<template>
<div class="app-container">
    <el-row :gutter="20">
      <el-col :span="10" :xs="24">
        <el-card class="box-card equalHeight">
          <div slot="header" class="clearfix">
            <i class="el-icon-s-order"></i>
            <span>合同信息</span>
          </div>
          <div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <svg-icon icon-class="people" class="svgIcon"/>创建人:
                <div class="pull-right">{{ contractInfo.createUserName }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="druid" class="svgIcon"/>创建人部门:
                <div class="pull-right">{{ contractInfo.createDeptName }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="guide" class="svgIcon"/>合同流水号:
                <div class="pull-right">{{ contractInfo.contractViewId }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="number" class="svgIcon"/>合同编号:
                <div class="pull-right">{{ contractInfo.contractNumber }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="time" class="svgIcon"/>合同创建日期:
                <div class="pull-right">{{ contractInfo.contractCreateTime }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="rate" class="svgIcon"/>付款方式:
                <div class="pull-right">{{ contractInfo.paymentTypeName }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="shopping" class="svgIcon"/>合同金额:
                <div class="pull-right"><span style="color: red;">{{ contractInfo.contractMoney }}元</span></div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="radio" class="svgIcon"/>是否缴费:
                <div class="pull-right">
                  <span style="color: rgb(0, 255, 13);" v-if="contractInfo.isPayment">已缴费🙂</span>
                  <span style="color: red;" v-else>未缴费😳!!</span>
                </div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="money" class="svgIcon"/>实付款:
                <div class="pull-right"><span style="color: rgb(252, 164, 0);">{{ contractInfo.realMoney }}元</span></div>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="14" :xs="24">
        <el-card class="box-card equalHeight">
          <div slot="header" class="clearfix">
            <i class="el-icon-lock"></i>
            <span>缴费凭证</span>
          </div>
          <div style="background-color: honeydew;">
            <img :src="requestShowDefaultImagePath" alt="暂无png或jpg格式的图片凭证文件!,或者您还未手动设置默认的消费凭证图片!" style="margin-left: 150px;text-align: center;width: 450px;height: 540px;"/>
          </div>
          </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="24">
        <el-card style="margin-top: 20px;width: 100%;">
          <div slot="header" class="clearfix">
            <i class="el-icon-reading"></i>
            凭证文件
          </div>
          <el-card v-if="imageFileData.length != 0" style="width: 100%;">
            <div slot="header" class="clearfix">
              <span><i class="el-icon-document-copy" style="margin-right: 20px;"><b>图片文件</b></i></span>
              <el-tag type="info"><i class="el-icon-question"></i>jpg,png 格式的缴费凭证文件列表</el-tag>
            </div>
              <el-table :data="imageFileData">
                <el-table-column align="center" width="50" type="index" label="序号"></el-table-column>
                <el-table-column align="center" width="230" prop="fileName" label="文件名"></el-table-column>
                <el-table-column align="center" width="100" prop="uploadName" label="上传人"></el-table-column>
                <el-table-column align="center" width="125" prop="fileSize" label="文件大小"></el-table-column>
                <el-table-column align="center" width="150" prop="fileType" label="文件类型"></el-table-column>
                <el-table-column align="center" width="200" prop="createTime" label="上传时间"></el-table-column>
                <el-table-column align="center" width="90" prop="isDefaultName" label="默认凭证?"></el-table-column>
                <el-table-column align="center" width="250" label="操作" class-name="small-padding fixed-width">
                  <template slot-scope="scope">
                    <el-tooltip class="item" effect="light" content="下载当前缴费凭证" placement="top">
                      <el-button type="warning" circle icon="el-icon-download" @click="dowmLoadFile(scope.row)"></el-button>
                    </el-tooltip>
                    <el-tooltip :disabled="scope.row.isDefault == '1'" class="item" effect="light" content="设置当前缴费凭证为默认凭证，可以在右上方看到该凭证" placement="top">
                      <el-button 
                        :disabled="scope.row.isDefault == '1'" 
                        style="margin-left: 30px;" 
                        type="success" 
                        circle 
                        icon="el-icon-s-tools"
                        @click="setCurrentOperationIsDefaultRowData(scope.row.rowId)"
                        ></el-button>
                    </el-tooltip>
                    <el-tooltip class="item" effect="dark" content="删除当前凭证(不可恢复!)" placement="top">
                      <el-button style="margin-left: 30px;" type="danger" circle icon="el-icon-delete" @click="deleteRow(scope.row.rowId)"></el-button>
                    </el-tooltip>
                  </template>
                </el-table-column>
              </el-table>
          </el-card>

          <el-card v-if="otherFileData.length != 0" style="margin-top: 20px;width: 100%;">
            <div slot="header" class="clearfix">
              <span><i class="el-icon-document-copy" style="margin-right: 20px;"><b>其他文件</b></i></span>
              <el-tag type="info"><i class="el-icon-question"></i>非jpg,png 格式的缴费凭证文件列表</el-tag>
            </div>
              <el-table :data="otherFileData">
                <el-table-column align="center" width="50" type="index" label="序号"></el-table-column>
                <el-table-column align="center" width="250" prop="fileName" label="文件名"></el-table-column>
                <el-table-column align="center" width="120" prop="uploadName" label="上传人"></el-table-column>
                <el-table-column align="center" width="125" prop="fileSize" label="文件大小"></el-table-column>
                <el-table-column align="center" width="150" prop="fileType" label="文件类型"></el-table-column>
                <el-table-column align="center" width="200" prop="createTime" label="上传时间"></el-table-column>
                <el-table-column align="center" width="300" label="操作" class-name="small-padding fixed-width">
                  <template slot-scope="scope">
                    <el-tooltip class="item" effect="light" content="下载当前缴费凭证" placement="top">
                      <el-button style="margin-left: 5px;" type="warning" circle icon="el-icon-download" @click="dowmLoadFile(scope.row)"></el-button>
                    </el-tooltip>
                    <el-tooltip class="item" effect="dark" content="删除当前凭证(不可恢复!)" placement="top">
                      <el-button style="margin-left: 30px;" type="danger" circle icon="el-icon-delete" @click="deleteRow(scope.row.rowId)"></el-button>
                    </el-tooltip>
                  </template>
                </el-table-column>
              </el-table>
          </el-card>

            <el-form ref="paymentCredenceForm">
              <el-card class="box-card" style="margin-top: 20px;" shadow="never">
                <div slot="header" class="clearfix">
                  <span><i class="el-icon-upload" style="margin-right: 20px;"><b>上传缴费凭证文件</b></i></span>
                  <el-tag type="info"><i class="el-icon-question"></i>可以上传jpg,png格式的图片，或者其他类型的文档文件!</el-tag>
                  <!-- <el-tooltip class="item" effect="dark" content="点击查看缴费凭证示例!" placement="top">
                      <el-button style="float: right;" type="success" round plain icon="el-icon-search" @click="showFileSuch">上传案例</el-button>
                  </el-tooltip> -->
                </div>
                
                  <!-- -- -->
                  <el-row>
                    <el-col :span="24">
                      <el-form-item style="text-align: center;margin-top: 20px;">
                        <el-upload
                          class="upload-demo"
                          ref="uploadAttachment"
                          :on-change="selectChangeFileList"
                          multiple
                          :limit="5"
                          :file-list="uploadFileList"
                          :on-exceed="handleExceed"
                          action=""
                          :http-request="customHandleUpload"
                          :auto-upload="false"
                          :show-file-list="true"
                          >
                        <el-button style="margin-left: 10px;" slot="trigger" size="small" icon="el-icon-search" >选择文件</el-button>
                        <el-button style="margin-left: 35px;" size="small" type="primary" icon="el-icon-upload2" @click="customHandleUpload">上传</el-button></el-button>
                      </el-upload>
                      </el-form-item>
                    </el-col>
                  </el-row>
              </el-card>
            </el-form>

        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="24" :xs="24">
        <el-card class="box-card" style="margin-top: 20px;width: 100%;">
          <div slot="header" class="clearfix">
            <i class="el-icon-s-order"></i>
            <span>缴费处理</span>
          </div>
          <el-card class="box-card" style="margin-top: 20px;width: 100%;">
            <div slot="header" class="clearfix">
              <i class="el-icon-s-order"></i>
              <span>更新金额</span></span>
            </div>
            <div v-if="! contractInfo.isPayment">
              <el-row>
                <el-col :span="8">
                    <p style="text-align: center;float: right;">合同金额为:<span style="font-size: 20px;"><b>{{ contractInfo.contractMoney }}元</b></span></p>
                </el-col>
                <el-col :span="6">
                  <p style="margin-left: 30px;text-align: center;">实际收款为:<span style="font-size: 20px;color: greenyellow;"><b>{{ contractInfo.realMoney }}元</b></span></p>
                </el-col>
                <el-col :span="4">
                  <p style="margin-left: 30px;text-align: center;float: left;">剩余未缴:<span style="font-size: 20px;color: red;"><b>{{ residueRealMoney }}元</b></span></p>
                </el-col>
              </el-row>
              <el-form ref="reference" label-suffix=":" label-width="150px" label-position="right">
                <el-row>  
                  <el-col :span="10">
                    <el-form-item style="margin-left: 250px;margin-top: 20px;text-align: center;" label="本次更新金额值">
                      <el-input style="width: 200px;text-align: center;" v-model.number="saveUpdateReamlMoney" placeholder="请输入实收款金额!"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item style="margin-top: 20px;margin-left: 20px">
                      <el-select style="text-align: center;margin-right: 100px" v-model="applyRealMoneyType">
                        <el-tooltip class="item" effect="dark" content="会把当前输入的金额，在原来的基础上增加" placement="top">
                          <el-option selected label="追加" value="append"></el-option>
                        </el-tooltip>
                      <el-tooltip class="item" effect="dark" content="会直接把实际金额替换为当前输入的金额" placement="bottom">
                        <el-option label="替换" value="replace"></el-option>
                      </el-tooltip>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="6">
                    <el-button style="margin-left: 30px;margin-top: 20px;" type="primary" @click="applyUpdateRealMoneyMethod()">更新并应用!</el-button>
                  </el-col>
                </el-row>
              </el-form>
            </div>
            
            <div v-else>
              <p style="text-align: center;font-size: 25px;"><span style="text-align: center;color: chartreuse;"><b><i>本业务已完成缴费！！</i></b></span></p>
            </div>

          </el-card>

          <el-card class="box-card" style="margin-top: 5px;width: 100%;">
            <div slot="header" class="clearfix">
              <i class="el-icon-s-order"></i>
              <span>缴费确认!</span>
            </div>
            <div style="text-align: center;">
              <el-button v-if="! contractInfo.isPayment" :disabled="!isCurrentPageSuccess" type="success" @click="handleSuccessPayment(contractInfo.contractViewId)">确认缴费完成!</el-button>
              <el-button v-else type="primary" @click="flushCurrentPage">返回上一页!</el-button>
            </div>
          </el-card>
        </el-card>
      </el-col>
    </el-row>


    <!-- 案例文件弹窗 -->
    <el-dialog 
      title="文件案例" 
      :visible.sync="showFileSuchOpen"
      width="900px" 
      center  
      append-to-body
      >
      
    </el-dialog>

    <el-backtop>
      <div
        style="{
          height: 100%;
          width: 100%;
          background-color: #f2f5f6;
          box-shadow: 0 0 6px rgba(0,0,0, .12);
          text-align: center;
          line-height: 40px;
          color: #1989fa;
        }"
      >
      ︿
    </div>
    </el-backtop>
</div>
</template>

<script>
  import { formatData } from '@/utils/proUtils'
  import { paymentCredenceFileUpload ,checkFile} from '@/api/business/fileModule'
  import 
  { getCategoryCredenceFileList,
    deleteCurrentOperationCredenceFile,
    setCurrentFileIsDefault ,
    updateRealMoneyApply ,
    confirmCurrentBusinessIsPaymentSuccess 
  } from '@/api/business/paymentCredence'
  import ruoyi from '@/utils/ruoyi'

export default {
 data() {
  return {
    businessCategory:'',
    contractInfo:{
      createUserName:'',
      createDeptName:'',
      contractViewId:'',
      contractNumber:'',
      contractMoney:0,
      contractCreateTime:'',
      isPayment:false,
      realMoney:-1,
      paymentType:'',
      paymentTypeName:'',
    },
    paymentType:[],
    saveUpdateReamlMoney:0, // 需要更改并应用的金额
    applyRealMoneyType: 'append',
    residueRealMoney: 0 ,   // 剩余的缴费金额
    isCurrentPageSuccess:false,
    // --------------文件系统相关
    isDefaultShowFile:undefined, // 默认显示的文件对象
    uploadFileList:[],
    showImageServerInterfacePath: '',
    requestShowDefaultImagePath:'www.baidu.com',
    imageFileData:[],
    otherFileData:[],

    showFileSuchOpen:false,
  };
 },

 watch: { 
     'contractInfo.contractViewId':{
       handler(newData,oldData){
         if(newData == undefined || newData == ''){
          this.$store.state.tagsView.visitedViews.splice(this.$store.state.tagsView.visitedViews.findIndex(item => item.path ===
          this.$route.path), 1)
          this.$router.push(this.$store.state.tagsView.visitedViews[this.$store.state.tagsView.visitedViews.length-1].path);
          console.log('当前页面空挡， 执行关闭！');
         }
       },
       deep:true,
     },
     'contractInfo.realMoney':{
        handler(newData,oldData){
          let currentRealMoney = parseInt(newData);
          this.residueRealMoney = parseInt(this.contractInfo.contractMoney) - currentRealMoney;
          if(this.residueRealMoney == 0){
            this.isCurrentPageSuccess = true;
          }
        },
        deep:true,
     },
     'isDefaultShowFile':{
       handler(newData,oldData){
          if(newData != undefined){
            let filePath = newData.credenceFilePath;
            let Base64 = require('js-base64').Base64;
            this.requestShowDefaultImagePath = this.showImageServerInterfacePath + Base64.encode(filePath+'');
          }else{
            this.requestShowDefaultImagePath = "www.baidu.com";
          }
       },
     },
  },
 created() {
    this.businessCategory =  this.$route.query.businessCategory;
    let rowData = this.$route.query.data;
    // console.log(rowData);
    this.contractInfo.contractViewId = rowData.rowId;
    this.contractInfo.contractNumber = rowData.contractNumber;
    this.contractInfo.contractCreateTime = formatData(rowData.createTime);
    this.contractInfo.realMoney = (rowData.realMoney == '' || rowData.realMoney == undefined) ? 0 : parseInt(rowData.realMoney);
    this.contractInfo.createDeptName = rowData.createDeptName;
    this.contractInfo.createUserName = rowData.createUserName;
    this.contractInfo.isPayment = rowData.isPaymentSuccess == '1';
    this.contractInfo.contractMoney = (rowData.contractMoney == '' || rowData.contractMoney == undefined) ? 0 : parseInt(rowData.contractMoney);
    this.contractInfo.paymentType = rowData.paymentType;
    // console.log(this.contractInfo);
    this.getPaymentTypeDict().then((response) => {
        this.paymentType = response.data;
        this.paymentType.forEach((value, index, array) => {
          if(value.dictValue == this.contractInfo.paymentType){
            this.contractInfo.paymentTypeName = value.dictLabel;
            return;
          }
        })
    });
    this.showImageServerInterfacePath = ruoyi.baseURL + ruoyi.showImagePath;
 },
 methods: {
    getFileList(){
      getCategoryCredenceFileList(this.contractInfo.contractViewId).then((response) => {
        let imageFileCredence = response.data.imageFileCredence;
        let otherFileCredence = response.data.otherFileCredence;
        /*
          rowId	"10ec5aed17f93d221a2244469c2ac37e"
          businessRowId	"314944425137595ab49764020ef20eb8"
          businessCategory	"request"
          realMoney	30
          paymentType	0
          confirmUserName	"admin"
          confirmUserNickname	"admin"
          credenceUploadFileName	"caoc.jpg"
          credenceFileType	"jpg"
          credenceFilePath	"C:/njtj/paymentCredencePath/2021-07-07/678269f1-ff05-43f8-bd3a-8d4dbd5827a8.jpg"
          credenceFileSize	"4.287"
          isDefault	0
          createTime	"2021-07-07 16:20:41"
        */

        imageFileCredence.forEach((value, index, array) => {
          if(value.isDefault == '1'){
            this.isDefaultShowFile = value;
          }
          this.imageFileData.push({
            fileName:value.credenceUploadFileName,
            uploadName:value.confirmUserNickname,
            createTime:value.createTime,
            fileType:value.credenceFileType,
            fileSize:value.credenceFileSize + ' KB',
            isDefaultName: (value.isDefault) == '0' ? '否' : '默认',
            //------------- 隐藏属性
            rowId:value.rowId,
            businessRowId:value.businessRowId,
            isDefault:value.isDefault,
            serverFilePath:value.credenceFilePath,
          });
        });
        otherFileCredence.forEach((value, index, array) => {
          this.otherFileData.push({
            fileName:value.credenceUploadFileName,
            uploadName:value.confirmUserNickname,
            createTime:value.createTime,
            fileType:value.credenceFileType,
            fileSize:value.credenceFileSize + ' KB',
            //-------------  隐藏属性
            rowId:value.rowId,
            businessRowId:value.businessRowId,
            isDefault:value.isDefault,
            serverFilePath:value.credenceFilePath,
          })
        });
      });
    },
    // 设置当前记录为默认 缴费凭证
    setCurrentOperationIsDefaultRowData(rowId){
      setCurrentFileIsDefault(rowId).then((response) => {
        if(response.code == 200){
          this.$message.success(response.msg);
        }else{
          this.$message.error(response.msg);
        }
        this.claerCurrentUploadFileListAndReload();
      })
    },
    // 删除当前记录
    deleteRow(rowId){
      this.$confirm('您确定要删除这条缴费凭证吗?(不可进行恢复)', "删除缴费凭证", {
          confirmButtonText: "确定(谨慎操作!)",
          cancelButtonText: "取消",
          center: true,
          type: "warning"
        }).then(function() {
          return deleteCurrentOperationCredenceFile(rowId);
        }).then((response) => {
            this.$message.success(response.msg);
            this.claerCurrentUploadFileListAndReload();
        })
    },
    // 显示上传缴费凭证文件案例
    showFileSuch(){
      this.showFileSuchOpen = true;
    },
    // 更新并应用当前的金额
    applyUpdateRealMoneyMethod(){
      if(! Number.isInteger(Number(this.saveUpdateReamlMoney)) || this.saveUpdateReamlMoney < 0){
        this.$message.error('您输入的数字不合法!')
        this.saveUpdateReamlMoney = 0;
        return;
      }
      let tempResidueRealMoney = this.residueRealMoney - this.saveUpdateReamlMoney;
      if(this.applyRealMoneyType == 'append'){    // 追加金额不能多余当前剩余未缴金额
        if(this.saveUpdateReamlMoney == 0){
          this.$message.error('追加金额不能为0元 !');
          this.saveUpdateReamlMoney = 0
          return;
        }
        if(tempResidueRealMoney < 0){
          this.$message.error('您追加的金额不能比剩余未缴费金额还要多 !');
          this.saveUpdateReamlMoney = 0
          return;
        }
      }else{                                      // 替换金额不能多余合同金额
        if(this.saveUpdateReamlMoney > parseInt(this.contractInfo.contractMoney)){
          this.$message.error('您替换的金额不能多于当前合同总金额 !');
          this.saveUpdateReamlMoney = 0
          return;
        }
      }

      let formData = new FormData();
      formData.append('businessRowId',this.contractInfo.contractViewId);
      formData.append('applyRealMoney',this.saveUpdateReamlMoney);
      formData.append('applyType',this.applyRealMoneyType);
      setTimeout(() => {
        
      }, 500);
      updateRealMoneyApply(formData).then((response) => {
        if(response.code == 200){
          this.$message.success(response.msg);
        }else{
          this.$message.error(response.msg);
        }
        this.flushCurrentPage();
      })
    },
    // 处理确认缴费
    handleSuccessPayment(businessRowId){
      this.$confirm('您确定当前业务已经清算全部的费用了吗?', "再次确认", {
          confirmButtonText: "已全部缴费",
          cancelButtonText: "取消",
          center: true,
          type: "warning"
        }).then(function() {
          return confirmCurrentBusinessIsPaymentSuccess(businessRowId);
        }).then((response) => {
          if(response.code == 200){
            this.$message.success(response.msg);
          }else{
            this.$message.error(response.msg);
          }
          this.flushCurrentPage();
        })
    },
    // 关闭当前页面
    flushCurrentPage(){
      this.$store.state.tagsView.visitedViews.splice(this.$store.state.tagsView.visitedViews.findIndex(item => item.path ===
          this.$route.path), 1);
        this.$router.push(this.$store.state.tagsView.visitedViews[this.$store.state.tagsView.visitedViews.length - 1].path)
    },
    // 清空当前文件列表和 文件列表数据并重新加载
    claerCurrentUploadFileListAndReload(){
      this.uploadFileList = [];
      this.$refs.paymentCredenceForm.resetFields();
      this.imageFileData = [];
      this.otherFileData = [];
      this.isDefaultShowFile = undefined;
      this.getFileList();
    },

  // -------------------------  操作文件方法
       // 改变文件列表触发钩子
    selectChangeFileList(file, fileList){
      this.uploadFileList = fileList;
      setTimeout(() => {
        
      }, 500);
    },
    // 执行自定义文件上传方法 
    customHandleUpload(){
      if(this.uploadFileList.length == 0){
        this.$message.error('您还未添加需要上传的文件!');
        return;
      }
      let formData = new FormData();
      formData.append('businessRowId',this.contractInfo.contractViewId);
      formData.append('businessCategory',this.businessCategory);
      formData.append('realMoney',this.contractInfo.realMoney);
      formData.append('paymentType',this.contractInfo.paymentType);
      this.uploadFileList.forEach((value, index, array) => {
        formData.append('paymentCredenceFile',value.raw);
      });
      paymentCredenceFileUpload(formData).then((response) => {
        if(response.code == 200 ){
          this.$message.success(response.msg);
          this.claerCurrentUploadFileListAndReload();
        }else{
          this.$message.error(response.msg);
          this.flushCurrentPage();
        }

      })
    },
    // 下载缴费凭证
    dowmLoadFile(row){
      let Base64 = require('js-base64').Base64;
      var val = Base64.encode(row.serverFilePath+'');
      const params={
        serverFilePath:val,
      }
      checkFile(params).then((response) => {
        if(response.code == 200){
            this.downloadFile(row.serverFilePath,row.fileName);
        }else{
            this.$message.error('文件不存在!,请联系管理员');
        }
      });
    },
    handleExceed(){
      this.$message.warning('最多上传五个文件,超出最大文件限制!');
    },
 },
 mounted() {
    this.getFileList();
  },
}
</script>

<style scoped>
  .equalHeight{
    height: 620px;
  }
  .svgIcon{
    margin-right: 7px;
  }
  .list-group-item{
    margin-top: 20px;
  }
  
</style>
