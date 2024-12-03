<template>
  <div>
    <el-card class="box-card">
      <div class="text item">
        <i class="el-icon-document-copy" style="margin-right: 10px"></i>
        委托业务检验报告申请
      </div>
    </el-card>

    <div style="margin: 20px"></div>
    <el-form
      ref="formItem"
      :model="formItem"
      :rules="formRules"
      label-suffix=":"
      label-width="150px"
      label-position="right"
      size="small"
    >
      <el-card class="marginTopCard box-card" shadow="never">
        <div slot="header" class="clearfix item">
          <span
            ><i class="el-icon-s-order" style="margin-right: 20px"
              ><b>表单信息</b></i
            ></span
          >
        </div>
        <el-row>
          <el-col :span="12">
            <el-form-item class="leftItem" label="申请人">
              <el-input
                :disabled="true"
                class="uniflItem"
                v-model="user.nickName"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item class="rightItem" label="申请部门">
              <el-input
                :disabled="true"
                class="uniflItem"
                v-model="user.deptName"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item
              class="leftItem"
              label="合同编号"
              prop="contractNumber"
            >
              <el-input class="uniflItem" v-model="formItem.contractNumber">
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              class="rightItem"
              label="报告编号"
              prop="reportNumber"
            >
              <el-input class="uniflItem" v-model="formItem.reportNumber">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item class="leftItem" label="委托单位" prop="entrustUnit">
              <el-input
                class="uniflItem"
                v-model="formItem.entrustUnit"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item class="rightItem" label="使用单位" prop="useUnit">
              <el-input class="uniflItem" v-model="formItem.useUnit"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item class="leftItem" label="联系电话" prop="phone">
              <el-input
                class="uniflItem"
                maxlength="11"
                show-word-limit
                v-model="formItem.phone"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item class="rightItem" label="联系人" prop="liaisons">
              <el-input
                class="uniflItem"
                v-model="formItem.liaisons"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row> </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item
              class="leftItem"
              label="设备类别"
              prop="deviceCategory"
            >
              <!-- :disabled="deviceTypeDisabled"  -->
              <el-select
                class="uniflItem"
                v-model="formItem.deviceCategory"
                placeholder="请选择"
              >
                <el-option
                  v-for="dict in deviceType"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item class="rightItem" label="经费(元)" prop="money">
              <el-input class="uniflItem" v-model.number="formItem.money">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item class="leftItem" label="报告模板选择">
              <el-select
                :disabled="isSelectedReportTemplate"
                class="specialItem"
                v-model="formItem.reportTemplateId"
                placeholder="请选择"
                @change="handleSelectReTem"
              >
                <el-option
                  v-for="template in reTemplateList"
                  :key="template.templateId"
                  :label="template.templateName"
                  :value="template.templateId"
                >
                </el-option>
              </el-select>
              
              <!-- 操作按钮 -->
              <!-- <el-dropdown
                v-if="isSelectedReportTemplate"
                @command="goReportTemplate($event)"
                trigger="click"
                size="small"
                style="margin-left: 20px"
              >
                <el-button
                  :disabled="handleReportBtn == true"
                  class="handleBtn"
                  type="success"
                  size="small"
                >
                  操作<i class="el-icon-arrow-down el-icon--right"></i>
                </el-button>
                <el-dropdown-menu slot="dropdown" :split-button="true">
                  <el-dropdown-item command="edit_generic"
                    >配置报告内容</el-dropdown-item
                  >
                  <el-dropdown-item command="edit_list"
                    >配置报告检验列表</el-dropdown-item
                  >
                  <el-dropdown-item command="view"
                    >查看完整报告</el-dropdown-item
                  >
                </el-dropdown-menu>
              </el-dropdown> -->
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <div v-if="isShowDeviceList == true">
        <DeviceList ref="deviceComponent" :deviceList="deviceList"></DeviceList>
      </div>
    </el-form>

    <div>
      <el-card class="marginTopCard box-card" shadow="never">
        <div slot="header" class="clearfix item">
          <span
            ><i class="el-icon-s-order" style="margin-right: 20px"
              ><b>操作</b></i
            ></span
          >
        </div>
        <el-button
          type="success"
          :disabled="(genericReportRowId == undefined || genericReportRowId == null || genericReportRowId == '') || isGenericReportLoading"
          v-loading="isGenericReportLoading"
          style="margin-left: 550px; width: 200px; height: 40px"
          @click="genericThisReport"
          >生成报告</el-button>
      </el-card>
    </div>
  </div>
</template>

<script>
import { getCurrentUserInfo } from "@/api/system/user";
import {
  getFormInfoReqRep,
  formSave,
  updateTemplateById,
} from "@/api/business/reqRepManage";
import { getDeviceById, setDeviceHandle } from "@/api/business/contractDevice";
import { operateReport } from "@/api/business/deviceTask";
import { getFormInfo } from "@/api/business/reqContReview";
import { getAllTemplate, list } from "@/api/customTemplate/reportTemplate";
import FormFooter from "@/views/core/common/footer/FormFooter";
import Rules from "@/views/core/validArray";
import DeviceList from "@/views/common/device/index";

export default {
  name: "requestCotractReview",
  components: {
    FormFooter,
    DeviceList,
  },
  data() {
    return {
      reviewDisable: true,
      // 表单校验
      formRules: {
        entrustUnit: [Rules.validArray.requireValue],
        useUnit: [Rules.validArray.requireValue],
        makeUnit: [Rules.validArray.requireValue],
        constructUnit: [Rules.validArray.requireValue],
        repairUnit: [Rules.validArray.requireValue],
        deviceName: [Rules.validArray.requireValue],
        reportTemplateName: [Rules.validArray.requireValue],
        deviceModel: [Rules.validArray.requireValue],
        productNumber: [Rules.validArray.requireValue],
        contractNumber: [Rules.validArray.requireValue],
        contractMoney: [Rules.validArray.requireValue],
        reportNumber: [Rules.validArray.requireValue],
        liaisons: [Rules.validArray.requireValue],
        reportIssue: [Rules.validArray.requireValue],
        money: [Rules.validArray.requireValue, Rules.validArray.numberValue],
        reportIssueNumber: [
          Rules.validArray.requireValue,
          Rules.validArray.numberValue,
        ],
        phone: [
          Rules.validArray.requireValue,
          Rules.validArray.inputPhoneNumberLength,
        ],
        devicePlace: [Rules.validArray.requireValue],

        checkUserName: [Rules.validArray.requireValueSelect],
        dutyCheckUserName: [Rules.validArray.requireValueSelect],
        dateOfManufacture: [
          Rules.validArray.requireValue,
          Rules.validArray.dateValue,
        ],
      },

      pickerOptions: {
        // 日期控制 , 约检日期不能在今天前边
        disabledDate(time) {
          return time.getTime() < Date.now();
        },
      },
      // 表单属性
      formItem: {
        rowId: "", //
        entrustUnit: "", //委托单位
        useUnit: "", //使用单位
        makeUnit: "", //制造单位
        constructUnit: "", //施工地址
        repairUnit: "", //维保单位
        liaisons: "", //联系人
        phone: "", //联系电话
        deviceCategory: "", //设备类别
        deviceModel: "", //设备型号
        productNumber: "", //产品编号
        dateOfManufacture: "", //出厂日期
        devicePlace: "", //设备使用地点
        dutyCheckUserId: "", // 责任检验员 id
        dutyCheckUserName: "", // 责任检验员 Name
        checkUserId: "", //检验员ID
        checkUserName: "", //检验员name
        reportNumber: "", //报告编号
        reportTemplateId: "",
        reportTemplateName: "", //检验报告模板
        contractNumber: "", //合同编号
        money: 0, //经费
        reportIssueNumber: "", //问题数目
        reportIssue: "", //报告书中问题描述
        attachmentResultIds: this.attachmentIds,
        contractId: "",
      },
      // -- report
      isSelectedReportTemplate: false,
      deviceRowId: "",
      genericReportRowId: "",
      isGenericReportLoading: false,

      oneType: false,
      twoType: false,
      user: {
        userId: "",
        userName: "",
        nickName: "",
        deptId: "",
        deptName: "",
      },
      reviewDisable: {
        advice_bmfzrsh: true, //部门负责人审核
        advice_fgldsh: true, //分管领导审核
      },
      formIsSuccess: true,
      contractId: "", //合同ID，
      //是否显示设备列表
      isShowDeviceList: false,
      //设备列表
      deviceList: [],
      // 设备种类 从 合同评审中获得值
      deviceType: [],

      //是否允许选择“报告模板选择”
      handleReportBtn: true,
      //检验报告模板
      reTemplateList: [],
    };
  },
  props: ["formData", "processReturnData", "attachmentIds"],
  watch: {
    processReturnData(newData, oldName) {
      this.formItemOther = newData;
      this.submitForm();
    },
    attachmentIds(newData, oldData) {
      // console.log(newData);
      this.formItem.attachmentResultIds = newData;
      // console.log('---'+this.formItem.attachmentResultIds);
    },
    "formItem.contractNumber": {
      handler(newData, oldData) {
        if (newData != undefined && newData != "") {
          this.$emit("set-report-type", newData);
        }
      },
      deep: false,
      immediate: false,
    },
    "formItem.deviceCategory": {
      handler(newData, oldData) {
        if (newData != undefined && newData != "") {
          // console.log(newData);
          list({ templateCategory: newData }).then((response) => {
            this.reTemplateList = response.rows;
            // console.log(this.reTemplateList);
          });
        }
      },
      deep: true,
      immediate: false,
    },
  },
  created() {
    this.getDeviceCateDict().then((response) => {
      this.deviceType = response.data;
    });
  },
  //---- mounted
  mounted() {
    // let Base64 = require('js-base64').Base64;
    // console.log(Base64.encode('测试附件上传AAA.docx'));
    // console.log(Base64.encode('测试附件上传AAA'));
    this.getCreateUserInfo();
    this.getCheckDataBycontractId();
    // this.getReportTemplate();
  },
  //---- methods
  methods: {
    /* 报告相关方法 */
    //获取所有报告模板列表
    // getReportTemplate() {
    //   getAllTemplate().then(response => {
    //     if(response.code === 200) {
    //       this.reTemplateList = response.data;
    //     }
    //   })
    // },
    //当选择报告模板后，将“操作按钮设置为可用”
    handleSelectReTem() {
      this.$confirm("您确定要选择当前模板吗(确认之后将无法再次更改！)?", {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        center: true,
        type: "warning",
      })
        .then(() => {
          // console.log(this.contractId);
          let formData = new FormData();
          formData.append("deviceId", this.deviceRowId);
          formData.append("contractRowId", this.contractId);
          formData.append("templateId", this.formItem.reportTemplateId);
          operateReport(formData).then((response) => {
            this.$message.success(
              "临时报告模板创建成功!，请点击生成报告"
            );
            this.genericReportRowId = response.data;
            // console.log(this.genericReportRowId);
            this.isSelectedReportTemplate = true;
          });
        })
        .catch(() => {
          this.formItem.reportTemplateId = "";
        });

        if (this.formItem.reportTemplateId != "") {
          this.handleReportBtn = false;
        }
    },
    genericThisReport() {
      this.isGenericReportLoading = true;
      let formData = new FormData();
      formData.append("rowId", this.genericReportRowId);
      formData.append("deviceId", this.deviceRowId);
      operateReport(formData).then((response) => {
        this.$message.success("生成成功!");
        this.$store.state.tagsView.visitedViews.splice(
          this.$store.state.tagsView.visitedViews.findIndex(
            (item) => item.path === this.$route.path
          ),
          1
        );
        this.$router.push(
          this.$store.state.tagsView.visitedViews[
            this.$store.state.tagsView.visitedViews.length - 1
          ].path
        );
      });
    },

    // 将编辑报告转移到 启动流程时
    goReportTemplate(name) {
      this.switchGoPage(name);
    },
    switchGoPage(name) {
      switch (name) {
        case "edit_generic":
          this.$router.push({
            name: "reportTempletGenericContent",
            params: {
              contractId: this.contractId,
              templateId: this.formItem.reportTemplateId,
              reportId: this.genericReportRowId,
            },
          });
          break;
        case "edit_list":
          this.$router.push({
            name: "reportTempletContent",
            params: {
              contractId: this.contractId,
              templateId: this.formItem.reportTemplateId,
              reportId: this.genericReportRowId,
            },
          });
          break;
        case "view":
          this.$router.push({
            name: "reportTempletContentView",
            params: {
              templateId: this.formItem.reportTemplateId,
              contractId: this.contractId,
              reportId: this.genericReportRowId,
            },
          });
          break;
      }
    },
    // ------------------------------------------------------
    getCreateUserInfo() {
      getCurrentUserInfo().then((response) => {
        this.user = response.data;
      });
    },
    returnFormValue() {
      this.returnValidValue();
      return new Promise((resolve, reject) => {
        const value = {
          flag: this.formIsSuccess,
        };
        resolve(value);
      });
    },
    // 表单提交 -------------
    returnValidValue() {
      let flag = false;
      this.$refs["formItem"].validate((valid) => {
        if (!valid) {
          this.$message.error("请检查填写数据是否有误!...");
        }
        this.formIsSuccess = valid;
      });
    },
    resetForm(formName) {
      if (confirm("您确定要重置当前表单吗?")) {
        this.$refs[formName].resetFields();
      } else {
        this.$message.warning("请谨慎操作");
      }
    },
    //从formData参数中判断是否存在contractId，若存在则说明是由合同流程直接发起的检验申请
    //并查询合同申请的信息，填充其中与检验申请字段一致的字段，并且显示设备列表
    getCheckDataBycontractId() {
      var cId = this.getDataByName("contractId");
      if (cId != "") {
        this.contractId = cId;
        this.isShowDeviceList = true;
        this.getContractWithCheck();
        this.getDeviceInfo();
        this.formItem.contractId = cId;
      }
    },
    //根据合同编号contractId查询合同信息
    getContractWithCheck() {
      getFormInfo(this.contractId).then((response) => {
        var noneKey = ["rowId", "createUserId", "createUserName"];
        var contractBean = response.data;
        var formKeys = Object.keys(this.formItem);
        //给合同申请中与检验申请中相同的字段赋值（rowId、createUserId、createUserName除外）
        for (var bean in contractBean) {
          if (formKeys.indexOf(bean) != -1 && noneKey.indexOf(bean) === -1) {
            this.formItem[bean] = contractBean[bean];
          }
        }
        this.formItem["reportCategory"] = contractBean.deviceCategory;
        //给检验申请赋值合同编号
        this.formItem["contractNumber"] = contractBean.contractNumber;
        //给检验申请赋值报告编号
        this.formItem["reportNumber"] = contractBean.contractNumber + "-BG";
        //经费
        this.formItem["money"] = parseInt(contractBean.contractMoney);
      });
    },
    //查询设备信息，当且仅当contractId存在时查询
    getDeviceInfo() {
      this.deviceRowId = this.getDataByName("deviceRowId");
      if (this.deviceRowId != "") {
        this.formItem["deviceRowId"] = this.deviceRowId;
        getDeviceById(this.deviceRowId).then((response) => {
          if (response.code === 200 && response.data != null) {
            this.deviceList.push(response.data);
          }
        });
      }
    },
    //根据名称获取formData中的参数
    getDataByName(name) {
      var retVal = "";
      if (this.formData.indexOf(name) != -1) {
        var keyVal = this.formData.split("&");
        for (var i = 0; i < keyVal.length; i++) {
          var bean = keyVal[i].split("=");
          if (bean[0] === name && bean[1] != "") {
            retVal = bean[1];
            break;
          }
        }
      }
      return retVal;
    },
  },
};
</script>

<style>
.text {
  font-size: 18px;
}

.item {
  margin-left: 10px;
  padding: 18px 0;
}
.el-date-editor.el-input,
.el-date-editor.el-input__inner {
  width: 300px;
}
.vue-treeselect__single-value {
  padding-left: 10px;
}
.box-card {
  width: 100%;
}
.el-input__inner {
  text-align: center;
}
.uniflItem {
  width: 300px;
  margin-left: 10px;
  text-align: center;
}
.specialItem {
  width: 775px;
  margin-left: 10px;
  text-align: center;
}
.leftItem {
  margin-left: 150px;
}
.rightItem {
  margin-right: 150px;
}

.marginTopCard {
  margin-top: 15px;
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
