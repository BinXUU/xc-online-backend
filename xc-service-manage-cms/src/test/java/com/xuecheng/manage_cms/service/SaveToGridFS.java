package com.xuecheng.manage_cms.service;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by BinXU on 2020/02/06.
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class SaveToGridFS {

    @Autowired
    GridFsTemplate gridFsTemplate;

    //将模板文件储存到GridGs
    @Test
    public void testGridFs() throws FileNotFoundException {
        //要存储的文件
        File file = new File("F:\\JAVA\\IdeaProjects\\JavaEE-Demo\\xcOnline\\xcEduUI01\\xc-ui-pc-static-portal\\course\\templates\\course.ftl");
        //定义输入流
        FileInputStream inputStram = new FileInputStream(file);
        //向GridFS存储文件
        ObjectId objectId = gridFsTemplate.store(inputStram, "课程详情测试模板", "");
        //得到文件ID
        String fileId = objectId.toString();
        System.out.println(fileId);
    }
}
