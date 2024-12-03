<template>
<div class="app-container">
    <el-row :gutter="24">
        <el-col :span="24" :xs="24">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>当前使用的签名</span>
            </div>

            <el-button type="warning" style="margin-bottom: 20px;margin-right: 130px;float: right;" @click="showUploadSignature">
              <span v-if="currentSignature.length == 0">上传签名</span>
              <span v-else>替换签名</span>
            </el-button>
            <!-- 

            rowId:value.rowId,
            userId:value.userId,
            userName:value.userName,
            signaturePath:value.signaturePath,
            signatureFileSize:value.signatureFileSize,
            signatureUploadName:value.signatureUploadName,
            createTime:value.createTime

             -->
            <el-table v-loading="currentSignatureLoading" max-height="500" :data="currentSignature" stripe border>
              <el-table-column label="签名照" align="center">
                <template  slot-scope="scope">
                  <img :src="imagePath(scope.row)" style="width: 150px;height: 40px;" alt="加载失败">
                </template>
              </el-table-column>
              <el-table-column prop="userName" align="center" label="签名人名称"></el-table-column>
              <el-table-column prop="createTime" align="center" label="上传时间"></el-table-column>
              <el-table-column prop="signatureUploadName" align="center" label="上传时名称"></el-table-column>
              <el-table-column prop="signatureFileSize" align="center" label="文件大小"></el-table-column>
              <el-table-column align="center" label="操作">
                <template slot-scope="scope">
                  <el-button type="primary" @click="dowmLoadFile(scope.row)">下载</el-button>
                </template>
            </el-table-column>
            </el-table>
          </el-card>  
        </el-col>
      </el-row>

      <el-row :gutter="24" style="margin-top: 30px;">
        <el-col :span="24" :xs="24">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>历史使用过的签名</span>
            </div>

            <el-table v-loading="historySignatureLoading" max-height="500" :data="historySignature" stripe border>
              <el-table-column label="签名照" align="center">
                <template  slot-scope="scope">
                  <img :src="imagePath(scope.row)" style="width: 150px;height: 40px;" alt="加载失败">
                </template>
              </el-table-column>
              <el-table-column prop="userName" align="center" label="签名人名称"></el-table-column>
              <el-table-column prop="createTime" align="center" label="上传时间"></el-table-column>
              <el-table-column prop="signatureUploadName" align="center" label="上传时名称"></el-table-column>
              <el-table-column prop="signatureFileSize" align="center" label="文件大小"></el-table-column>
              <el-table-column align="center" label="操作">
                  <template slot-scope="scope">
                      <el-button type="primary" @click="dowmLoadFile(scope.row)">下载</el-button>
                      <el-button type="danger" @click="deleteRow(scope.row.rowId)">删除历史</el-button>
                  </template>
              </el-table-column>
            </el-table>
        </el-card>  
      </el-col>
    </el-row>

    <el-dialog 
      title="上传新签名" 
      :visible.sync="uploadSinature"
      width="900px" 
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      center  
      append-to-body
      >
      <el-form ref="signatureForm">
        <el-card class="box-card" shadow="never">
          <div slot="header" class="clearfix item">
            <span><i class="el-icon-upload" style="margin-right: 20px;"><b>上传电子签名</b></i></span>
            <el-tag type="info"><i class="el-icon-question"></i>只能上传jpg格式的电子签名</el-tag>
          </div>
            <!-- -- -->
            <el-row>
              <el-col :span="24">
                <el-form-item label="签名上传" style="margin-left: 20px;margin-top: 20px;">
                  <el-upload
                    class="upload-demo"
                    ref="uploadAttachment"
                    :on-change="selectChangeFileList"
                    :before-remove="beforeRemove"
                    :on-remove="handleRemove"
                    :on-success="uploadSuccess"
                    :limit="1"
                    :file-list="uploadFileList"
                    :on-exceed="handleExceed"
                    action=""
                    :http-request="uploadSinatureFileList"
                    :auto-upload="false"
                    :show-file-list="true"
                    >
                  <el-button style="margin-left: 10px;" slot="trigger" size="small" icon="el-icon-search" >选择文件</el-button>
                  <el-button style="margin-left: 35px;" size="small" type="primary" icon="el-icon-upload2" @click="uploadSinatureFileList">上传</el-button>
                </el-upload>
                </el-form-item>
              </el-col>
            </el-row>
        </el-card>
      </el-form>
    </el-dialog>
 </div>
</template>

<script>

import { getCurrentUserSignature,deleteSignatureById } from "@/api/system/signature";
import { signatureFileUpload , checkFile} from "@/api/business/fileModule";
import ruoyi from '@/utils/ruoyi'

export default {
 data() {
    return {
      // local valid
      uploadSinature: false,
      showImageServerInterfacePath: '',
      uploadFileList:[],
      currentSignatureLoading:true,
      historySignatureLoading:true,
      // server response`
      currentSignature:[],
      historySignature:[],
    };
  },

  props:['insertFlag'],

  watch: {
    // 编辑报告模板时 会使用该组件当子组件来插入 电子签名
    insertFlag(newData,oldData){
      // console.log('signature component -->' + this.insertFlag);
      // console.log('signature component newData-->' + newData);
      // console.log('signature component oldData-->' + oldData);
      if(newData){
        if(this.currentSignature.length == 0){
          console.log(this.currentSignature);
          this.$message.warning('您当前还没有电子签名，请上传!');
          this.$emit('handleInsertSignature', undefined);
        }else{
          let row = this.currentSignature[0];
          let resultFilePath = this.imagePath(row);
          this.$emit('handleInsertSignature', resultFilePath);
        }

      }
    },
  },
  created() {
    this.showImageServerInterfacePath = ruoyi.baseURL + ruoyi.showImagePath;
  },
  mounted() {
    this.getCurrentUserSignatureImages();
  },
  methods: {
    // server methods  获取当前用户的电子签名列表 ， 当前使用的和历史使用的
    getCurrentUserSignatureImages(){
      getCurrentUserSignature().then((response) => {
        let responseHistoryList = response.data.historySignature
        let responseCurrentUseList = response.data.currentSignature
        responseCurrentUseList.forEach((value, index, array) => {
          this.currentSignature.push({
            rowId:value.rowId,
            userId:value.userId,
            userName:value.userName,
            signaturePath:value.signaturePath,
            signatureFileSize:value.signatureFileSize + '(KB)',
            signatureUploadName:value.signatureUploadName,
            createTime:value.createTime
          });
        });
        this.currentSignatureLoading = false;
        setTimeout(() => {
          responseHistoryList.forEach((value, index, array) => {
          this.historySignature.push({
            rowId:value.rowId,
            userId:value.userId,
            userName:value.userName,
            signaturePath:value.signaturePath,
            signatureFileSize:value.signatureFileSize + '(KB)',
            signatureUploadName:value.signatureUploadName,
            createTime:value.createTime
          });
        });
        this.historySignatureLoading = false;
        }, 1200);
      });
    },
    // 生成显示图片的访问路径
    imagePath(row){
      let Base64 = require('js-base64').Base64;
      let resultImagePath = this.showImageServerInterfacePath + Base64.encode(row.signaturePath+'');
      return resultImagePath;
    },
    // 下载电子签名
    dowmLoadFile(row){
      let Base64 = require('js-base64').Base64;
      var val = Base64.encode(row.signaturePath+'');
      const params={
        serverFilePath:val,
      }
      checkFile(params).then((response) => {
        if(response.code == 200){
            this.downloadFile(row.signaturePath,row.signatureUploadName);
        }else{
            this.$message.error('文件不存在!,请联系管理员');
        }
      });
    },
    // 删除历史记录
    deleteRow(id){
      this.$confirm('您确定要删除这条历史记录吗?(不可进行恢复)', "删除历史记录", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return deleteSignatureById(id);
        }).then((response) => {
            this.$message.success(response.msg);
            this.flushCurrentPage();
        })
    },

    //------------------------------------- 文件组件内部操作 local methods 
    // 改变文件列表触发钩子
    selectChangeFileList(file, fileList){
      // console.log(file);
      // console.log(fileList);
      let tempUploadFileList = fileList.slice(0);
      if(tempUploadFileList.length != 0){
        tempUploadFileList.forEach((value, index, array) => {
        this.uploadFileList.push(value.raw);
        });
      }
      setTimeout(() => {
        
      }, 200);
    },
    // 上传文件
    uploadSinatureFileList(){
      if(this.uploadFileList.length == 0){
        this.$message.error('您还未选中电子签名!');
        return;
      }
      if(this.beforeAvatarUpload(this.uploadFileList[0])){
        let formData = new FormData();
        formData.append("signatureFile",this.uploadFileList[0]);
        signatureFileUpload(formData).then((response) => {
          if(response.code == 200){
            this.$message.success(response.msg)
          }else{
            this.$message.error(response.msg)
          }
          this.uploadSinature = false;
          this.uploadFileList = [];
          this.flushCurrentPage();
        });
      }
      

    },
    // 显示上传签名dialog
    showUploadSignature(){
      this.uploadSinature = true;
      this.uploadFileList = [];
    },
    beforeAvatarUpload(file) {
        const isJPG = file.type === 'image/jpeg';
        const isLt2M = file.size / 1024 / 1024 < 1;

        if (!isJPG) {
          this.$message.error('上传头像图片只能是 JPG 格式!');
        }
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 1MB!');
        }
        return isJPG && isLt2M;
    },
    uploadSuccess(){
      this.uploadSinature = false;
      this.$message.success('上传电子签名成功!');
      this.uploadFileList = [];
    },
    handleExceed(){
      this.$message.warning('您只能上传一个电子签名!');
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${ file.name }？`);
    },
    handleRemove(){
      this.uploadFileList = [];
    },
    flushCurrentPage(){
      this.$store.state.tagsView.visitedViews.splice(this.$store.state.tagsView.visitedViews.findIndex(item => item.path ===
          this.$route.path), 1);
        this.$router.push(this.$store.state.tagsView.visitedViews[this.$store.state.tagsView.visitedViews.length - 1].path)
        this.$router.go(-1);
    }

  }
}
</script>

<style>

</style>
