<template>
  <div>
    <el-form ref="attachmentForm">
      <el-card class="box-card" shadow="never">
        <div slot="header" class="clearfix item">
          <span><i class="el-icon-upload" style="margin-right: 20px;"><b>附件</b></i></span>
          <el-tag type="info"><i class="el-icon-question"></i>每次上传单个文件最大为10MB!,总上传不超过80MB!</el-tag>
        </div>
          <!-- -- -->
          <el-row>
            <el-col :span="24">
              <el-form-item v-if="attachmentBtnShow" label="附件上传" style="margin-left: 400px;margin-top: 20px;">
                <el-upload
                  class="upload-demo"
                  ref="uploadAttachment"
                  :on-change="handleChangeAttachment"
                  multiple
                  action=""
                  :http-request="attachmentUpload"
                  :file-list="attachmentList"
                  :auto-upload="false"
                  :show-file-list="false"
                  >
                <el-button style="margin-left: 10px;" slot="trigger" size="small" icon="el-icon-search" >选择文件</el-button>
                <el-button style="margin-left: 35px;" :loading="fileUploading" size="small" type="primary" icon="el-icon-upload2" @click="attachmentUpload('uploadBook')">全部上传</el-button></el-button>
                <!--     <div slot="tip" class="el-upload__tip">只能上传<span style="color: red;">doc,docx</span>文件大小，且不超过40MB</div>:headers="head" -->
              </el-upload>
              </el-form-item>
            </el-col>
          </el-row>
      </el-card>
    </el-form>
        <!-- fileTable -->
        <el-table
        v-if="attachmentTableShow"
        border
        max-height="270"
        style="width: 100%;"
        :data="selectAttachmentListData">
          <el-table-column type="index" label="序号" align="center" width="80"></el-table-column>
          <el-table-column prop="name" label="文件名称" align="center" width="380"></el-table-column>
          <el-table-column prop="createTime" label="上传时间" align="center" width="232"></el-table-column>
          <el-table-column prop="uploadUser" label="上传人" align="center" width="200"></el-table-column>
          <el-table-column prop="uploadFlag" label="文件状态" align="center" width="155"></el-table-column>
          <el-table-column label="操作" align="center" width="239">
            <template slot-scope="scope">
                <el-button :disabled="(scope.row.uploadFlag == '已上传!')" v-if="scope.row.prevFlag == '1' " class="handleBtn" type="danger" size="small" @click="beforeRemove(scope.row)">删除</el-button>
                <el-button v-if="scope.row.prevFlag == '0'" class="handleBtn" type="warning" size="small" @click="showFileInfo(scope.row)">查看</el-button>
            </template>
          </el-table-column>
        </el-table>
    </div>
</template>

<script>
  import { getCurrentTime } from '@/utils/proUtils'
  import { businessFileUpload,getFileListByIds,checkFile } from "@/api/business/fileModule"

export default {
    name: 'attachmentComponent',
    data() {
        return {
            // 本次表单的 附件 列表属性
            attachmentList:[],
            attachmentResultIds:'',
            // 展示列表格
            selectAttachmentListData:[],

            attachmentBtnShow:true,
            attachmentTableShow: false,
            waitUploadFileCount:0,
            fileUploading:false,
            fileShow:true,
            fileDelete:true,
            fileSizes:0,
            // 组件传值
            /*
                processType
                createUserNickName
            */
            propParams:this.attachmentParam,

            // 之前流程文件
            previousAttachmentFile:[],
            previousAttachmentLength: 0,
        }
    },
    props:{
        attachmentParam:Object,
        previousIds:String
    },
    watch: {
        // 监测 table 的显示开关
        selectAttachmentListData(newData,oldData){
            this.attachmentTableShow = (newData.length != 0);
        },
        attachmentResultIds(newData,oldData){
          // console.log('ids 发生变化-->' + newData);
            this.$emit('child-return-ids', newData);
        },
        previousIds(newData,oldData){
            // console.log('new attachment watch previousIds-->' + newData);
            // console.log('old attachment watch previousIds-->' + oldData);
              if(this.propParams.processType != 'start'){
                this.loadPreviousStepFile(newData);
              }
        }
    },
    mounted() {
      this.judgeFileOperation(this.propParams.processType);
    },
    methods: {
        //0 变动文件列表
        handleChangeAttachment(file, fileList) {
          this.attachmentList = fileList.slice(0);
          const currentDate = getCurrentTime();
          this.selectAttachmentListData.push({
              name: file.name,
              createTime: currentDate+'',
              uploadUser: this.propParams.createUserNickName,
              uploadFlag:'未上传',

              // 隐藏属性
              uid:file.uid, // 文件uid
              prevFlag:'1', // 是否为本次流程上传的文件标识  0 为之前流程  1 为本此流程
          });
          // console.log(this.attachmentList);
          this.attachmentList.forEach((value, index, array) => {
             this.fileSizes += parseInt(value.size);
          })
          // console.log('fileSize' + this.fileSizes);
        },
        //1. 删除文件之前执行方法
        beforeRemove(row) {
        // console.log(row)
        this.$confirm('确定移除文件: ' + row.name+' 吗？','提示',{
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "error"
          }).then(()=>{
            this.handleRemove(row);
          }).catch((value) => {
            this.$message.warning('您取消了本次删除文件');
          })
      },
      // 2 真正执行删除方法的操作方法
      /*
          删除 选中文件列表中的文件的同时 真实的上传文件的列表 也会被同步删除掉
      */
      handleRemove(row) {
        // console.log("当前文件列表长度-->" + this.previousAttachmentLength);
        if(this.previousAttachmentLength == 0){
            this.attachmentList.forEach((value, index, array) => {
            if(value.uid == row.uid){
              this.attachmentList.splice(index,1);
              this.selectAttachmentListData.splice(index,1);
              this.$message.success("您删除了选中文件:"+row.name);
            }
          });
        }else{
          this.attachmentList.forEach((value, index, array) => {
            if(value.uid == row.uid){
              this.attachmentList.splice(index,1);
              this.selectAttachmentListData.splice((index + (parseInt(this.previousAttachmentLength))),1);
              this.$message.success("您删除了选中文件:"+row.name);
            }
          });
        }
      },
      // 提交的时候检查是否还存在已选中 但是未提交的文件
      checkHasWaitAttachment(){
        var uploadFileCount = 0;
        let uploadFileList = this.selectAttachmentListData.slice(this.previousAttachmentLength);
        this.attachmentList.forEach((value, index, array) => {
          if(uploadFileList[index].uploadFlag == '未上传'){
            ++uploadFileCount;
          }
        });
        // debugger;
        let flag = true;
        if(uploadFileCount != 0){
          flag = false;
          this.$message.warning('您还存在有未上传的文件,请先提交选中的文件');
        }
        return new Promise((resolve,reject)=>{
            const value = {
              flag:flag
            }
            // console.log(flag);
            resolve(value);
       });

      },
      // 3 附件上传
      attachmentUpload(){
        if(this.attachmentList.length == 0){
          this.$message.warning('您还未添加需要上传的文件!');
          return;
        }
        this.$confirm('请您检查是否为您想要上传的文件(文件一经上传不可删除!)', '提示', {
          confirmButtonText: '上传!',
          cancelButtonText: '检查文件',
          type: 'warning',
          center: true
        }).then(() => {
          this.attachmentUploadInnderMethod();
        }).catch((value) => {
          return;
        })
       },
      attachmentUploadInnderMethod(){
        let formData = new FormData();
        const category = this.propParams.category;
        // 需要上传文件的 个数
        var uploadFileCount = 0;
        let uploadFileList = this.selectAttachmentListData.slice(this.previousAttachmentLength);
        // console.log('upload file list ----> ' + uploadFileList);
        this.attachmentList.forEach((value, index, array) => {
            if(uploadFileList[index].uploadFlag == '未上传'){
              ++uploadFileCount;
              formData.append("uploadWork",this.attachmentList[index].raw);
            }
        });
        // console.log(this.fileSizes);
        if(this.attachmentList.length == 1 && parseInt(this.fileSizes) > 10455040){
          this.$message.error('您上传的单个文件太大!');
          // console.log(this.fileSizes);
          return;
        }
        if(this.attachmentList.length > 1 && parseInt(this.fileSizes) > 83886080){
          this.$message.error('您上传的多个文件太大!');
          // console.log(this.fileSizes);
          return;
        }
        // console.log('当前需要进行文件上传的文件个数为 --- > ' + uploadFileCount);
        if(uploadFileCount != 0){
          this.fileUploading = true;
          formData.append("category",category);
          businessFileUpload(formData).then((response) => {
            if(response.code == 200){
            this.fileUploading = false;
            this.$message.success('上传附件成功!');
            this.attachmentResultIds += (response.data + ',');
            this.selectAttachmentListData.forEach((value, index, array) => {
              value.uploadFlag = '已上传!';
            });
            // console.log('附件上传之后的 resultIds -- >' + this.attachmentResultIds);
            }
         })
        }else{
          this.$message.success('当前文件列表所有文件已全部上传!');
        }
      },
       // 4.加载上一次上传的文件数据
      loadPreviousStepFile(newData){
          if(newData != null && newData != ''){
            const params = {
                attachmentResultIds: newData
            }
            getFileListByIds(params).then((response) => {
              if(response.code === 0){
                const previousAttachmentList = response.data.previousAttachmentList;
                this.installCurrentFileTableData(previousAttachmentList);
              }
            });
          }
        },
        //5 装载参数
        // 组装本次 流程的文件列表数据
      installCurrentFileTableData(previousAttachmentList){
        this.previousAttachmentLength = parseInt(previousAttachmentList.length);
        // console.log('长度' + this.previousAttachmentLength);
        if(this.previousAttachmentLength != 0){
            previousAttachmentList.forEach((item) =>{
                this.selectAttachmentListData.push({
                name: item.attachmentName,
                createTime: item.createTime,
                uploadUser: item.createUserName,
                uploadFlag:'已上传!',
                // 隐藏属性
                serverFilePath:item.filePath,
                rowId:item.rowId,
                fileAttribute:item.fileAttribute,
                createUserName:item.createUserName,
                prevFlag:'0',
                });
            });
        }
      },
       // 6. 查看文件 文件下载
       // 文件查看 -- 下载
        showFileInfo(row){
            //  console.log(row);
            let Base64 = require('js-base64').Base64;
            var val = Base64.encode(row.serverFilePath+'');
            const params={
              serverFilePath:val,
            }
            checkFile(params).then((response) => {
            if(response.code == 200){
                this.downloadFile(row.serverFilePath,row.name);
            }else{
                this.$message.error('文件不存在!,请联系管理员');
            }
        })
       },
       // 5. 根据当前流程状态来 判断对文件的操作 ， 和附件部分组件的显示状态进行改变
       judgeFileOperation(processType){
         if(processType == 'start' || processType == 'update'){
            this.attachmentBtnShow = true;
            this.attachmentTableShow = false;
         }else if(processType == 'view'){
            this.attachmentBtnShow = false;
            this.attachmentTableShow = true;
         }
       },


    },
}
</script>


