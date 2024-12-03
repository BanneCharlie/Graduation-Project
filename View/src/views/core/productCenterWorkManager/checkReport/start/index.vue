<template>
    <div>
      <el-card class="box-card">
        <div class="text item">
          <i class="el-icon-document-copy" style="margin-right: 10px;"></i>
          产品中心检验报告申请
        </div>
      </el-card>
  
      <div style="margin: 20px;"></div>
      <el-form ref="formItem" :model="formItem" :rules="formRules" label-suffix=":" label-width="150px" label-position="right"
        size="medium">
        <el-card class="marginTopCard box-card" shadow="never">
          <div slot="header" class="clearfix item">
            <span><i class="el-icon-s-order" style="margin-right: 20px;"><b>表单信息</b></i></span>
          </div>
          <el-row>
            <el-col :span="12">
              <el-form-item class="leftItem" label="申请人">
                <el-input :disabled="true" class="uniflItem " v-model="user.nickName"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="rightItem" label="申请部门">
                <el-input :disabled="true" class="uniflItem " v-model="user.deptName"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item class="leftItem" label="委托单位" prop="entrustUnit">
                <el-input class="uniflItem" v-model="formItem.entrustUnit"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="rightItem" label="使用单位" prop="useUnit">
                <el-input class="uniflItem" v-model="formItem.useUnit"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <!-- -- -->
          <el-row>
            <el-col :span="12">
              <el-form-item class="leftItem" label="制造单位" prop="makeUnit">
                <el-input class="uniflItem " v-model="formItem.makeUnit"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="rightItem" label="联系人" prop="liaisons">
                <el-input class="uniflItem " v-model="formItem.liaisons"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <!-- -- -->
          <el-row>
            <el-col :span="12">
              <el-form-item class="leftItem" label="联系电话" prop="phone">
                <el-input class="uniflItem " v-model="formItem.phone"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="rightItem" label="产品名称" prop="deviceName">
                <el-input class="uniflItem " v-model="formItem.deviceName"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <!-- -- -->
          <el-row>
            <el-col :span="12">
              <el-form-item class="leftItem" label="产品型号" prop="deviceModel">
                <el-input class="uniflItem " v-model="formItem.deviceModel"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="rightItem" label="产品编号" prop="productNumber">
                <el-input class="uniflItem " v-model="formItem.productNumber"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <!-- -- -->
          <el-row>
            <el-col :span="12">
              <el-form-item class="leftItem" label="出厂日期" prop="dateOfManufacture">
                <div class="uniflItem">
                  <el-date-picker v-model="formItem.dateOfManufacture" format="yyyy 年 MM 月 dd 日">
                  </el-date-picker>
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="rightItem" label="产品使用地点" prop="devicePlace">
                <el-input class="uniflItem " v-model="formItem.devicePlace"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item class="leftItem" label="责任检验员" prop="dutyCheckUserName">
                <el-input :disabled="true" class="uniflItem " v-model="formItem.dutyCheckUserName">
                  <i @click="clickSelectPeopleDialog('one')" slot="suffix"
                     class="el-input__icon el-icon-search" style="cursor: pointer;"></i>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="rightItem" label="检验员" prop="checkUserName">
                <el-input :disabled="true" class="uniflItem " v-model="formItem.checkUserName">
                  <i @click="clickSelectPeopleDialog('two')" slot="suffix"
                     class="el-input__icon el-icon-search" style="cursor: pointer;"></i>
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
  
          <el-row>
            <el-col :span="12">
              <el-form-item class="leftItem" label="报告编号" prop="reportNumber">
                <el-input class="uniflItem " v-model="formItem.reportNumber">
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="rightItem" label="检验报告模板" prop="reportTemplateName">
                <el-input class="uniflItem " v-model="formItem.reportTemplateName">
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
              <el-form-item  class="textareaStyle" label="部门负责人审核">
                <el-input :disabled="reviewDisable.advice_bmfzrsh"
                          class="intputTextarea" type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :xl="24" :xs="24">
              <el-form-item class="textareaStyle" label="分管领导审核" >
                <el-input :disabled="reviewDisable.advice_fgldsh"
                          class="intputTextarea" type="textarea"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

        </el-card>
      </el-form>
      <el-dialog title="请选择检验员" :visible.sync="selectPeopleDialog">
      <SelectPeople ref="selectPeople"></SelectPeople>
      <div slot="footer" class="dialog-footer" style="margin-top: 15px; text-align: center;">
        <el-button type="primary" @click="handleSaveDialog">确 定</el-button>
        <el-button @click="cancelDialog('two')">取 消</el-button>
      </div>
      </el-dialog>
      <el-dialog title="请选择责任检验员" :visible.sync="selectOnePeopleDialog">
        <SelectPeople ref="selectOnePeople"></SelectPeople>
        <div slot="footer" class="dialog-footer" style="margin-top: 15px; text-align: center;">
          <el-button type="primary" @click="handleOneSaveDialog">确 定</el-button>
          <el-button @click="cancelDialog('one')">取 消</el-button>
        </div>
      </el-dialog>
    </div>
  
  </template>
  
  <script>
    import { getCurrentUserInfo } from "@/api/system/user";
    import { formSave } from "@/api/business/proRepManage";
    import FormFooter from '@/views/core/common/footer/FormFooter'
    import Rules from '@/views/core/validArray'
    import SelectPeople from '@/views/components/dialog/SelectPeople'
  
    export default {
      name: 'requestCotractReview',
      components: {
        FormFooter,
        SelectPeople
      },
      data() {
        return {
          reviewDisable: true,
          // 表单校验
          formRules:{
            entrustUnit:[Rules.validArray.requireValue],
            useUnit:[Rules.validArray.requireValue],
            makeUnit:[Rules.validArray.requireValue],
            constructUnit:[Rules.validArray.requireValue],
            repairUnit:[Rules.validArray.requireValue],
            deviceName:[Rules.validArray.requireValue],
            reportTemplateName:[Rules.validArray.requireValue],
            deviceModel:[Rules.validArray.requireValue],
            productNumber:[Rules.validArray.requireValue],
            contractNumber:[Rules.validArray.requireValue],
            contractMoney:[Rules.validArray.requireValue],
            reportNumber:[Rules.validArray.requireValue],
            liaisons:[Rules.validArray.requireValue],
            reportIssue:[Rules.validArray.requireValue],
            money:[Rules.validArray.requireValue,Rules.validArray.numberValue],
            reportIssueNumber:[Rules.validArray.requireValue,Rules.validArray.numberValue],
            phone:[Rules.validArray.requireValue,Rules.validArray.inputPhoneNumberLength],
            devicePlace:[Rules.validArray.requireValue],
            // 邮编
            checkUserName:[Rules.validArray.requireValueSelect],
            dutyCheckUserName:[Rules.validArray.requireValueSelect],
            dateOfManufacture:[Rules.validArray.requireValue,Rules.validArray.dateValue]
          },
  
          pickerOptions: {
            // 日期控制 , 约检日期不能在今天前边
            disabledDate(time) {
              return time.getTime() < Date.now();
            },
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
            attachmentResultIds: this.attachmentIds
          },
          user: {
            userId: '',
            userName: '',
            nickName: '',
            deptId: '',
            deptName: ''
          },
          reviewDisable: {
            advice_bmfzrsh: true, //部门负责人审核
            advice_fgldsh: true //分管领导审核
          },
          formIsSuccess: true,
          selectPeopleDialog: false, //选择人员树形穿梭框是否显示
          selectOnePeopleDialog:false, // 选择责任检验员穿梭框
        };
      },
      //---- methods
      methods: {
        getCreateUserInfo() {
          getCurrentUserInfo().then(response => {
            this.user = response.data
          })
        },
        returnFormValue() {
          return new Promise((resolve,reject)=>{
             const value = {
                 flag:this.formIsSuccess
             }
             resolve(value);
          })
        },
        // 表单提交 -------------
        submitForm(formName) {
          var subData = {
            entity: this.formItem
          }
          for(var item in this.processReturnData) {
            subData[item] =  this.processReturnData[item]
          }
          formSave(subData).then(response => {
            if(response.code === 200) {
              this.$emit('form-submit-return', response)
            }
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
        resetForm(formName) {
          if (confirm('您确定要重置当前表单吗?')) {
            this.$refs[formName].resetFields();
          } else {
            this.$message.warning("请谨慎操作");
          }
        },
        clickSelectPeopleDialog(type) {
          if(type == 'one'){
            this.selectOnePeopleDialog = true;
          }else if(type == 'two'){
            this.selectPeopleDialog = true
          }
        },
        handleSaveDialog() {
          var selectPeopleData = this.$refs.selectPeople.getSelectPeopleByDialog();
          var ids = '';
          var names = '';
          for(var i = 0; i < selectPeopleData.length; i++) {
            var bean = selectPeopleData[i]
            ids += bean.userId + ',';
            names += bean.username + ',';
          }
          this.formItem.checkUserId = ids.substring(0, ids.length - 1);
          this.formItem.checkUserName = names.substring(0, names.length - 1);
          this.selectPeopleDialog = false;
        },
      handleOneSaveDialog(){
        var selectPeopleData = this.$refs.selectOnePeople.getSelectPeopleByDialog();
        var ids = '';
        var names = '';
        for(var i = 0; i < selectPeopleData.length; i++) {
          var bean = selectPeopleData[i]
          ids += bean.userId + ',';
          names += bean.username + ',';
        }
        this.formItem.dutyCheckUserId = ids.substring(0, ids.length - 1);
        this.formItem.dutyCheckUserName = names.substring(0, names.length - 1);
        this.selectOnePeopleDialog = false;
      },
      cancelDialog(type) {
        if(type == 'one'){
          this.$refs.selectOnePeople.resetToData();
          this.selectOnePeopleDialog = false;
        }else if(type == 'two'){
          this.$refs.selectPeople.resetToData();
          this.selectPeopleDialog = false;
        }
      }
      },
      watch: {
        processReturnData(newData, oldName) {
          this.formItemOther = newData
          this.submitForm()
        },
        attachmentIds(newData,oldData){
          // console.log(newData);
          this.formItem.attachmentResultIds = newData
          // console.log('---'+this.formItem.attachmentResultIds);
        }
      },
      //---- mounted
      mounted() {
        this.getCreateUserInfo();
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
  