<template>
  <div>
    <el-card class="box-card">
      <div class="text item">
        <i class="el-icon-document-copy" style="margin-right: 10px;"></i>
        委托业务检验报告申请
      </div>
    </el-card>

    <div style="margin: 20px;"></div>
    <el-form ref="formItem" :model="formItem" label-suffix=":" label-width="150px" label-position="right"
      size="small">
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
            <el-form-item class="leftItem" label="合同编号" prop="contractNumber">
              <el-input :disabled="true" class="uniflItem " v-model="formItem.contractNumber">
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item class="rightItem" label="报告编号" prop="reportNumber">
              <el-input :disabled="true" class="uniflItem " v-model="formItem.reportNumber">
              </el-input>
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
            <el-form-item class="leftItem" label="经费" prop="money">
              <el-input :disabled="true" class="uniflItem " v-model.number="formItem.money">
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item class="rightItem" label="问题数目" prop="reportIssueNumber">
              <el-input :disabled="true" class="uniflItem " v-model.number="formItem.reportIssueNumber">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item class="leftItem" label="报告模板选择">
              <el-select :disabled="true" class="uniflItem" v-model="formItem.reportTemplateId" placeholder="请选择" @change="handleSelectReTem">
                <el-option
                  v-for="template in reTemplateList"
                  :key="template.templateId"
                  :label="template.templateName"
                  :value="template.templateId"
                  >
                </el-option>
              </el-select>

              <el-button @click="goReportTemplate('view')" style="margin-left: 20px;"  class="handleBtn" type="success" size="small">
                查看报告<i class="el-icon-s-promotion el-icon--right"></i>
              </el-button>
              <!-- <el-dropdown @command="goReportTemplate($event)" trigger="click" size="small" style="margin-left: 20px;">
                <el-button :disabled="handleReportBtn == true"  class="handleBtn" type="success" size="small">
                  查看报告<i class="el-icon-arrow-down el-icon--right"></i>
                </el-button>
                <el-dropdown-menu slot="dropdown" :split-button="true">
                  <el-dropdown-item command="edit_generic">配置报告封面</el-dropdown-item>
                  <el-dropdown-item command="edit_list">配置报告检验列表</el-dropdown-item>
                  <el-dropdown-item command="view">查看完整报告</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown> -->
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item class="leftItem" label="报告书中问题描述" prop="reportIssue">
              <el-input :disabled="true" class="uniflItem" style="width: 775px;" v-model="formItem.reportIssue">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <div v-if="isShowDeviceList == true">
        <DeviceList ref="deviceComponent" :deviceList="deviceList"></DeviceList>
      </div>

      <el-card class="marginTopCard box-card" shadow="never">
        <div slot="header" class="clearfix item">
          <span><i class="el-icon-s-order" style="margin-right: 20px;"><b>评审信息</b></i></span>
        </div>
        <el-row>
          <el-col :xl="24" :xs="24">
            <el-form-item  class="textareaStyle" label="部门负责人审核" >
              <el-input :disabled="reviewDisable.advice_bmfzrsh"
                        class="intputTextarea" type="textarea"
                        v-model="adviceMsg.advice_bmfzrsh"
						@dblclick.native="showDefaultOpinions('advice_bmfzrsh')"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xl="24" :xs="24">
            <el-form-item class="textareaStyle" label="分管领导审核" >
              <el-input :disabled="reviewDisable.advice_fgldsh"
                        class="intputTextarea" type="textarea"
                        v-model="adviceMsg.advice_fgldsh"
						@dblclick.native="showDefaultOpinions('advice_fgldsh')"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>
      <AdviceWord ref="adviceWord" v-on:setQuickOpinion="setQuickOpinion"></AdviceWord>
    </el-form>
  </div>
</template>

<script>
  import { getFormInfoReqRep, formSave, updateTemplateById } from '@/api/business/reqRepManage'
  import { getCurrentUserInfo  } from "@/api/system/user";
  import { getFormPrivilege } from "@/api/formPrivilege";
  import { getAdviceMsg } from "@/api/process/process";
  import { getDeviceById, setDeviceHandle } from '@/api/business/contractDevice'
  import { getAllTemplate } from '@/api/customTemplate/reportTemplate'
  import AdviceWord from '@/views/common/adviceWord/index'
  import DeviceList from '@/views/common/device/index'

  export default {
    name: 'requestCotractReview',
    components: {
      AdviceWord,
      DeviceList
    },
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
          checkUserName: '',//检验员
          checkUserId: '',//检验员
          reportNumber: '', //报告编号
          reportTemplateId:'',
          reportTemplateName: '',//检验报告模板
          contractNumber: '',//合同编号
          attachmentResultIds: '',
          money: '', //经费
          reportIssueNumber: '',//问题数目
          reportIssue: '', //报告书中问题描述
          createUserName: '',
          createDeptName: '',
          contractId:'',
        },
        fileUpdateFlag: false,
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
        checkUsers: [],
        //是否为待处理任务
        blfs: '0',
        currentAdvice: '',//当前流程步骤id，如bmfzrsh
        //是否显示设备列表
        isShowDeviceList: false,
        //设备列表
        deviceList: [],
        //流程是否是最后一步
        isEndNode: false,
        //是否允许选择“报告模板选择”
        handleReportBtn: true,
        //检验报告模板
        reTemplateList: [],
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
        if(!this.fileUpdateFlag){
          this.formItem.attachmentResultIds = '';
        }
        var subData = {
          entity: this.formItem
        }
        var adviceList = [];
        for (var item in this.formItemOther) {
          subData[item] = this.formItemOther[item]
        }
        for (var advice in this.adviceMsg) {
          if(advice === this.currentAdvice) {
            subData[advice] = this.adviceMsg[advice];
          } else {
             subData[advice] = '';
          }
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
        getFormInfoReqRep(this.formItem.rowId).then(response => {
          if (response.code === 200) {
            this.formItem = response.data;

            this.getFormPrivilege();
            this.$emit('previous-step-ids', this.formItem.attachmentResultIds);
            this.isShowDeviceList = true;
            this.getDeviceList();
          }
        })
      },
      getFormPrivilege() {
        getFormPrivilege({taskId: this.taskId}).then(response => {
          if (response.code === 200) {
            for(var name in response.data) {
              if(response.data[name] == 'W') {
                this.reviewDisable[name] = false;
                if(name.startsWith("advice_")) {
                  //说明是当前审批人应该输入的意见框
                  this.currentAdvice = name;
                }
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
      },
      //打开快捷意见选择框
      showDefaultOpinions(type) {
        if(this.reviewDisable[type] == false) {
          this.$refs.adviceWord.changeDialog(true);
        }
      },
      //根据快捷意见选择框选择的值设置当前流程意见输入框
      setQuickOpinion(opinion) {
        this.adviceMsg[this.currentAdvice] = opinion;
      },
      //查询设备信息，当且仅当contractId存在时查询
      getDeviceList() {
        getDeviceById(this.formItem.deviceRowId).then(response => {
          if(response.code === 200 && response.data != null) {
            this.deviceList.push(response.data);
          }
        })
      },
      isProcessEndNode() {
        this.isEndNode = true
      },
      /* 报告相关方法 */
      //获取所有报告模板列表
      getReportTemplate() {
        getAllTemplate().then(response => {
          if(response.code === 200) {
            this.reTemplateList = response.data;
          }
        })
      },
      //当选择报告模板后，将“操作按钮设置为可用”
      handleSelectReTem() {
        if(this.formItem.reportTemplateId != '') {
          this.handleReportBtn = false;
        }
      },
      goReportTemplate(name) {
        var dataSourceId = '';
        for(var i = 0; i < this.reTemplateList.length; i++) {
          var bean = this.reTemplateList[i];
          if(bean.templateId === this.formItem.reportTemplateId) {
            dataSourceId = bean.dataSourceId;
          }
        }
        updateTemplateById(this.formItem.rowId, this.formItem.reportTemplateId).then(response => {
          if(response.code === 200) {
            this.switchGoPage(name, dataSourceId);
          }
        })
      },
      switchGoPage(name, dataSourceId) {
        switch (name) {
          case 'edit_generic':
            this.$router.push({
              name: 'reportTempletGenericContent',
              params: {
                dataSourceId: dataSourceId,
                templateId: this.formItem.reportTemplateId,
              }
            });
            break
          case 'edit_list':
            this.$router.push({
              name: 'reportTempletContent',
              params: {
                dataSourceId: dataSourceId,
                templateId: this.formItem.reportTemplateId
              }
            });
            break
          case 'view':
            setDeviceHandle(this.formItem.deviceRowId, this.formItem.rowId).then(response => {
              if(response.data == true) {
                this.$router.push({
                  name: 'reportTempletContentView',
                  params: {
                    templateId: this.formItem.reportTemplateId,
                    reqCheckId: this.formItem.rowId,
                    contractId: this.formItem.contractId,
                  }
                });
              }
            })
            break
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
        this.fileUpdateFlag = true;
        this.formItem.attachmentResultIds = newData;
        // console.log('update页面的 本次上传文件的 ids -- >' + newData);
        // console.log('update 页面的 本次附件ids --- >' + this.formItem.attachmentResultIds);
      },
    },
    //---- mounted
    mounted() {
      this.getFormRowId();
      this.getAdviceMsg();
      this.getReportTemplate();
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
  .el-date-editor.el-input, .el-date-editor.el-input__inner {
    width: 300px;
  }
  .vue-treeselect__single-value{
    padding-left: 10px;
  }
  .box-card {
    width: 100%;
  }
  .el-input__inner{
    text-align: center;
  }
  .uniflItem{
      width: 300px;
      margin-left: 10px;
      text-align: center;
  }
  .leftItem{
      margin-left: 150px;
  }
  .rightItem{
      margin-right: 150px;
  }

  .marginTopCard {
  margin-top: 15px;
  }
  .textareaStyle{
    width: 800px;
    height: 80px;
    margin-left: 150px;
  }
  .intputTextarea{
    margin-left: 20px;
  }
  .intputTextareaRules{
    margin-left: 20px;
  }
  .handleCardBtn{
    margin-top: 100px;
  }
</style>
