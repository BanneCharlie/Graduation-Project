<template>
    <div>
      <el-card class="box-card">
        <div class="text item">
          <i class="el-icon-document-copy" style="margin-right: 10px;"></i>
          产品中心检验报告申请
        </div>
      </el-card>
  
      <div style="margin: 20px;"></div>
      <el-form ref="formItem" :model="formItem" label-suffix=":" label-width="150px" label-position="right"
        size="medium">
        <el-card class="marginTopCard box-card" shadow="never">
          <div slot="header" class="clearfix item">
            <span><i class="el-icon-s-order" style="margin-right: 20px;"><b>表单信息</b></i></span>
          </div>
          <el-row>
            <el-col :span="12">
              <el-form-item class="leftItem" label="申请人">
                <el-input :disabled="true" class="uniflItem " v-model="formItem.createUserName"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="rightItem" label="申请部门">
                <el-input :disabled="true" class="uniflItem " v-model="formItem.createDeptName"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item class="leftItem" label="委托单位" prop="entrustUnit">
                <el-input :disabled="true" class="uniflItem" v-model="formItem.entrustUnit"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="rightItem" label="使用单位" prop="useUnit">
                <el-input :disabled="true" class="uniflItem" v-model="formItem.useUnit"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <!-- -- -->
          <el-row>
            <el-col :span="12">
              <el-form-item class="leftItem" label="制造单位" prop="makeUnit">
                <el-input :disabled="true" class="uniflItem " v-model="formItem.makeUnit"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="rightItem" label="施工单位" prop="constructUnit">
                <el-input :disabled="true" class="uniflItem " v-model="formItem.constructUnit"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <!-- -- -->
          <el-row>
            <el-col :span="12">
              <el-form-item class="leftItem" label="维保单位" prop="repairUnit">
                <el-input :disabled="true" class="uniflItem " v-model="formItem.repairUnit"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="rightItem" label="联系人" prop="liaisons">
                <el-input :disabled="true" class="uniflItem " v-model="formItem.liaisons"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <!-- -- -->
          <el-row>
            <el-col :span="12">
              <el-form-item class="leftItem" label="联系电话" prop="phone">
                <el-input :disabled="true" class="uniflItem " v-model="formItem.phone"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="rightItem" label="设备名称" prop="deviceName">
                <el-input :disabled="true" class="uniflItem " v-model="formItem.deviceName"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <!-- -- -->
          <el-row>
            <el-col :span="12">
              <el-form-item class="leftItem" label="设备型号" prop="deviceModel">
                <el-input :disabled="true" class="uniflItem " v-model="formItem.deviceModel"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="rightItem" label="产品编号" prop="productNumber">
                <el-input :disabled="true" class="uniflItem " v-model="formItem.productNumber"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <!-- -- -->
          <el-row>
            <el-col :span="12">
              <el-form-item class="leftItem" label="出厂日期" prop="dateOfManufacture">
                <div class="uniflItem">
                  <el-date-picker :disabled="true" v-model="formItem.dateOfManufacture" format="yyyy 年 MM 月 dd 日">
                  </el-date-picker>
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="rightItem" label="设备使用地点" prop="devicePlace">
                <el-input :disabled="true" class="uniflItem " v-model="formItem.devicePlace"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item class="leftItem" label="责任检验员" prop="dutyCheckUserName">
                <el-input :disabled="true" class="uniflItem " v-model="formItem.dutyCheckUserName">
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="rightItem" label="检验员" prop="checkUserName">
                <el-input :disabled="true" class="uniflItem " v-model="formItem.checkUserName">
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
  
          <el-row>
            <el-col :span="12">
              <el-form-item class="leftItem" label="报告编号" prop="reportNumber">
                <el-input :disabled="true" class="uniflItem " v-model="formItem.reportNumber">
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="rightItem" label="检验报告模板" prop="reportTemplateName">
                <el-input :disabled="true" class="uniflItem " v-model="formItem.reportTemplateName">
                  <i slot="suffix" class="el-input__icon el-icon-search"></i>
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>
  
        <el-card class="marginTopCard box-card" shadow="never">
          <div slot="header" class="clearfix item">
            <span><i class="el-icon-s-order" style="margin-right: 20px;"><b>评审信息</b></i></span>
          </div>
          <el-row>
            <el-col :xl="24" :xs="24">
              <el-form-item  class="textareaStyle" label="部门负责人审核" >
                <el-input :disabled="reviewDisable.advice_bmfzrsh"
                          class="intputTextarea" type="textarea"
                          v-model="adviceMsg.advice_bmfzrsh"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :xl="24" :xs="24">
              <el-form-item class="textareaStyle" label="分管领导审核" >
                <el-input :disabled="reviewDisable.advice_fgldsh"
                          class="intputTextarea" type="textarea"
                          v-model="adviceMsg.advice_fgldsh"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>
      </el-form>
    </div>
  
  
  </template>
  
  <script>
    import { getFormInfo, formSave } from '@/api/business/proRepManage'
    import { getCurrentUserInfo  } from "@/api/system/user";
    import { getFormPrivilege } from "@/api/formPrivilege";
    import { getAdviceMsg } from "@/api/process/process";
  
    export default {
      name: 'requestCotractReview',
      data() {
        return {
          isAuthority: 0, //是否开启权限，start页面不用开启
          reviewDisable: {
            advice_bmfzrsh: true, //部门负责人审核
            advice_fgldsh: true //分管领导审核
          },
          adviceMsg: {
            advice_bmfzrsh: '',
            advice_fgldsh: '',
          },
          // 表单属性
          formItem: {
            rowId: '', //
            entrustUnit: '', //委托单位
            useUnit: '', //使用单位
            makeUnit: '', //制造单位
            constructUnit: '', //施工地址
            repairUnit: '', //维保单位
            liaisons: '', //联系人
            phone: '', //联系电话
            deviceName: '', //设备名称
            deviceModel: '', //设备型号
            productNumber: '', //产品编号
            dateOfManufacture: '', //出厂日期
            devicePlace: '', //设备使用地点
            dutyCheckUserId:'', // 责任检验员 id
            dutyCheckUserName:'', // 责任检验员 Name
            checkUserId: '', //检验员ID
            checkUserName: '',//检验员name
            reportNumber: '', //报告编号
            reportTemplateName: '',//检验报告模板
            contractNumber: '',//合同编号
            money: '', //经费
            reportIssueNumber: '',//问题数目
            reportIssue: '', //报告书中问题描述
            createUserName: '',
            createDeptName: '',
            attachmentResultIds:''
          },
          formItemOther: {},

          pickerOptions: {
            // 日期控制 , 约检日期不能在今天前边
            disabledDate(time) {
              return time.getTime() < Date.now();
            },
          },
  
          formResult: {},
          goParam: {},
          taskId: '',
        };
      },
      //---- methods
      methods: {
        returnFormValue(){
          this.returnValidValue();
          return new Promise((resolve,reject)=>{
              const value = {
                  flag:this.formIsSuccess
              }
              resolve(value);
         })
        },
        returnValidValue(){
          let flag = false;
          this.$refs['formItem'].validate((valid) => {
              if (!valid) {
                this.$message.error('请检查填写数据是否有误!...');
              }
              this.formIsSuccess =  valid;
  
          });
        },
        // 表单提交 -------------
        submitForm() {
          var subData = {
            entity: this.formItem
          }
          var adviceList = [];
          for (var item in this.formItemOther) {
            subData[item] = this.formItemOther[item]
          }
          for (var advice in this.adviceMsg) {
            subData[advice] = this.adviceMsg[advice]
          }
          formSave(subData).then(response => {
            if (response.code === 200) {
              this.formResult = response;
            }
          })
        },
        getFormRowId() {
          var keyVal = this.formData.split("&");
          for(var i = 0; i < keyVal.length; i++) {
            var temp = keyVal[i].split("=");
            this.goParam[temp[0]] = temp[1]
          }
          this.formItem.rowId = this.goParam.rowId;
          this.taskId = this.goParam.taskId
          this.getFormInfo();
        },
        getFormInfo() {
          getFormInfo(this.formItem.rowId).then(response => {
            if (response.code === 200) {
              this.formItem = response.data;
              this.getFormPrivilege();
              this.$emit('previous-step-ids', this.formItem.attachmentResultIds);
            }
          })
        },
        getFormPrivilege() {
          getFormPrivilege({taskId: this.taskId}).then(response => {
            if (response.code === 200) {
              for(var name in response.data) {
                if(response.data[name] == 'W') {
                  this.reviewDisable[name] = false;
                }
              }
            }
          })
        },
        getAdviceMsg() {
          for(var name in this.adviceMsg) {
            getAdviceMsg({tableId: this.formItem.rowId, signControlName: name.split("_")[1]}).then(response => {
              if (response.code === 200) {
                for(var i = 0; i < response.data.length; i++) {
                  var item = response.data[i];
                  this.adviceMsg['advice_' + item.controlName] = item.optionContext
                }
              }
            })
          }
        }
      },
      watch: {
        processReturnData(newData, oldName) {
          this.formItemOther = newData
          this.submitForm();
        },
        formResult(newData, oldName) {
          this.$emit('form-submit-return', newData)
        },
        attachmentIds(newData,oldData){
          this.formItem.attachmentResultIds = newData;
          // console.log('update页面的 本次上传文件的 ids -- >' + newData);
          // console.log('update 页面的 本次附件ids --- >' + this.formItem.attachmentResultIds);
        }
      },
      //---- mounted
      mounted() {
        this.getFormRowId();
        this.getAdviceMsg();
      },
      props: ['formData', 'processReturnData','attachmentIds']
    }
  </script>
  
  <style>
    .text {
      font-size: 18px;
    }
  
    .item {
      margin-left: 10px;
      padding: 18px 0;
    }
  
    .box-card {
      width: 100%;
    }
  
    .uniflItem {
      width: 220px;
      margin-left: 10px;
    }
  
    .leftItem {
      margin-left: 150px;
    }
  
    .rightItem {
      margin-right: 150px;
    }
  
    .marginTopCard {
    margin-top: 30px;
  }
  
    .textareaStyle {
      width: 800px;
      height: 80px;
      margin-left: 150px;
    }
  
    .intputTextarea {
      margin-left: 20px;
    }
  
    .intputTextareaRules {
      margin-left: 20px;
    }
  
    .handleCardBtn {
      margin-top: 100px;
    }
  </style>
  