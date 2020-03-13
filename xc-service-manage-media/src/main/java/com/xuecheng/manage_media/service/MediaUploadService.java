package com.xuecheng.manage_media.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.domain.media.response.MediaCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_media.config.RabbitMQConfig;
import com.xuecheng.manage_media.dao.MediaFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

/**
 * Created by BinXU on 2020/02/12.
 */

@Slf4j
@Service
public class MediaUploadService {

    @Autowired
    MediaFileRepository mediaFileRepository;

    //上传文件的目录
    @Value("${xc-service-manage-media.upload-location}")
    String uploadPath;

    @Value("${xc-service-manage-media.mq.routingkey-media-video}")
    String routingkey_media_video;

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 根据文件的md5获取文件路径
     * 规则：
     * 一级目录：md5的第一个字符
     * 二级目录：md5的第二个字符
     * 三级目录：md5
     * 文件名：MD5+文件扩展名
     *
     * @param fileMd5 文件md5
     * @param fileExt 文件扩展名
     * @return 文件路径
     */
    private String getFilePath(String fileMd5, String fileExt) {
        return uploadPath + fileMd5.substring(0, 1) + "/" +
                fileMd5.substring(1, 2) + "/" + fileMd5 + "/" + fileMd5 +
                "." + fileExt;
    }

    //得到文件目录的想对路径，路径中去掉根目录
    private String getFileFolderRelativePath(String fileMd5, String fileExt) {
        return fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) +
                "/" + fileMd5 + "/";
    }

    //得到文件所在目录
    private String getFileFolderPath(String fileMd5) {
        return uploadPath + fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/";
    }

    //得到块文件所在目录
    private String getChunkFileFolderPath(String fileMd5) {
        String chunkFileFolder = getFileFolderPath(fileMd5) + "chunks/";
        File file = new File(chunkFileFolder);
        if (!file.exists()) {
            file.mkdirs();
        }
        return chunkFileFolder;
    }

    //创建文件目录
    private boolean createFileFolder(String fileMd5) {
        //创建上传目录
        String fileFolderPath = getFileFolderPath(fileMd5);
        File fileFolder = new File(fileFolderPath);
        if (!fileFolder.exists()) {
            //创建文件夹
            return fileFolder.mkdirs();
        }
        return true;
    }

    //文件上传注册
    public ResponseResult register(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        //检查文件是否存在
        //1.获取文件路径
        String filePath = getFilePath(fileMd5, fileExt);
        File file = new File(filePath);

        //2.查询数据库文件是否存在
        Optional<MediaFile> optional = mediaFileRepository.findById(fileMd5);
        if (file.exists() && optional.isPresent()) {
            //文件存在直接返回
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_EXIST);
        }
        boolean fileFolder = createFileFolder(fileMd5);
        if (!fileFolder) {
            //上传文件目录创建失败
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_FAIL);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //检查块文件
    public CheckChunkResult checkchunk(String fileMd5, Integer chunk, Integer chunkSize) {
        //得到块文件目录
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        //块文件的文件名以1,2,3..序号命名，没有扩展名
        File chunkFile = new File(chunkFileFolderPath + chunk);
        if (chunkFile.exists()) {
            return new CheckChunkResult(MediaCode.CHUNK_FILE_EXIST_CHECK, true);
        } else {
            return new CheckChunkResult(MediaCode.CHUNK_FILE_EXIST_CHECK, false);
        }
    }

    //上传块文件
    public ResponseResult uploadchunk(MultipartFile file, String fileMd5, Integer chunk) {
        if (file == null) {
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_ISNULL);
        }
        //创建块文件目录
        boolean fileFolder = createFileFolder(fileMd5);
        File chunkfile = new File(getChunkFileFolderPath(fileMd5) + chunk);
        //上传块文件
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = file.getInputStream();
            outputStream = new FileOutputStream(chunkfile);
            IOUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("upload chunk file fail:{}", e.getMessage());
            ExceptionCast.cast(MediaCode.CHUNK_FILE_UPLOAD_FAIL);
        } finally {
            try {
                assert inputStream != null;
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                assert outputStream != null;
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //创建块文件目录
    private boolean createchunkFileFolder(String fileMd5) {
        //创建上传文件目录
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        File chunkFileFolder = new File(chunkFileFolderPath);
        if (!chunkFileFolder.exists()) {
            //创建文件夹
            return chunkFileFolder.mkdirs();
        }
        return true;
    }

    //合并块文件
    public ResponseResult mergechunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        //获取块文件路径
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        File chunkFileFolder = new File(chunkFileFolderPath);
        if (!chunkFileFolder.exists()) {
            chunkFileFolder.mkdirs();
        }
        //合并文件路径
        File mergeFile = new File(getFilePath(fileMd5, fileExt));
        //创建合并文件
        //若合并文件存在 先删除再创建
        if (mergeFile.exists()) {
            mergeFile.delete();
        }
        boolean newFile = false;
        try {
            newFile = mergeFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("mergechunks..create mergeFile fail:{}", e.getMessage());
        }
        if (!newFile) {
            ExceptionCast.cast(MediaCode.MERGE_FILE_CREATEFAIL);
        }
        //获取块文件，此列表为已经排好序的列表
        List<File> chunkFiles = getChunkFiles(chunkFileFolder);
        //合并文件
        File mergefile = getMergeFile(mergeFile, chunkFiles);
        if (mergefile == null) {
            ExceptionCast.cast(MediaCode.MERGE_FILE_FAIL);
        }
        //校验文件
        boolean checkResult = checkFileMd5(mergefile, fileMd5);
        if (!checkResult) {
            ExceptionCast.cast(MediaCode.MERGE_FILE_CHECKFAIL);
        }
        //将文件信息保存到数据集
        MediaFile mediaFile = new MediaFile();
        mediaFile.setFileId(fileMd5);
        mediaFile.setFileName(fileMd5 + "." + fileExt);
        mediaFile.setFileOriginalName(fileName);
        //文件路径保存相对路径
        mediaFile.setFilePath(getFileFolderRelativePath(fileMd5, fileExt));
        mediaFile.setFileSize(fileSize);
        mediaFile.setUploadTime(new Date());
        mediaFile.setMimeType(mimetype);
        mediaFile.setFileType(fileExt);
        //设置状态为上传成功
        mediaFile.setFileStatus("301002");
        MediaFile save = mediaFileRepository.save(mediaFile);
        //删除分块文件
        deletePartFiles(chunkFileFolder);
        //向MQ发送视频处理消息
        return this.sendProcessVideoMsg(save.getFileId(), fileMd5);
    }

    //向MQ发送视频处理消息
    public ResponseResult sendProcessVideoMsg(String mediaId, String fileMd5) {
        Optional<MediaFile> optional = mediaFileRepository.findById(fileMd5);
        if (!optional.isPresent()){
            return new ResponseResult(CommonCode.FAIL);
        }
        MediaFile mediaFile = optional.get();
        //发送视频处理消息
        Map<String, String> msgMap = new HashMap<>();
        msgMap.put("mediaId", mediaId);
        //发送的消息
        String msg = JSON.toJSONString(msgMap);
        try {
            this.rabbitTemplate.convertAndSend(RabbitMQConfig.EX_MEDIA_PROCESSTASK, routingkey_media_video, msg);
            log.info("send media process task msg:{}", msg);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("send media process task error,msg is:{},error:{}", msg, e.getMessage());
            return new ResponseResult(CommonCode.FAIL);
        }
        return new ResponseResult(CommonCode.SUCCESS);

    }

    //上传成功后删除分块文件
    private void deletePartFiles(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }
            for (File childFile : childFiles) {
                childFile.delete();
            }
            file.delete();
        }
    }

    //检验文件的Md5值
    private boolean checkFileMd5(File mergeFile, String md5) {
        if (mergeFile == null || StringUtils.isEmpty(md5)) {
            return false;
        }
        //进行md5校验
        FileInputStream mergeFileInputstream = null;
        try {
            mergeFileInputstream = new FileInputStream(mergeFile);
            //得到文件的md5
            String mergeFileMd5 = DigestUtils.md5Hex(mergeFileInputstream);
            //比较md5
            if (md5.equalsIgnoreCase(mergeFileMd5)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("checkFileMd5 error,file is:{},md5 is:{}", mergeFile.getAbsoluteFile(), md5);
        } finally {
            try {
                mergeFileInputstream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //获取所有块文件
    private List<File> getChunkFiles(File chunkFileFolder) {
        //获取路径下的所有块文件
        File[] chunkFiles = chunkFileFolder.listFiles();
        //将文件数组转成list，并推荐
        List<File> chunkFileList = new ArrayList<>();
        chunkFileList.addAll(Arrays.asList(chunkFiles));
        //排序
        Collections.sort(chunkFileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())) {
                    return 1;
                }
                return -1;
            }
        });
        return chunkFileList;
    }

    //合并文件
    private File getMergeFile(File mergeFile, List<File> chunkFiles) {
        try {
            //创建输出流
            FileOutputStream outputStream = new FileOutputStream(mergeFile);
            //创建文件缓冲区
            byte[] b = new byte[1024];
            //合并文件
            for (File chunkFile : chunkFiles) {
                //创建输入流
                FileInputStream inputStream = new FileInputStream(chunkFile);
                int len = -1;
                //读取分块文件
                while ((len = inputStream.read(b)) != -1) {
                    //向输出流写数据
                    outputStream.write(b, 0, len);
                }
                inputStream.close();
            }
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("merge file error:{}", e.getMessage());
            return null;
        }
        return mergeFile;
    }

}
