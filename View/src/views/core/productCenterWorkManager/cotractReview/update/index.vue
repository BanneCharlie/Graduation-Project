<template>
  <div>
    <el-card class="box-card">
      <div class="text item">
        <i class="el-icon-document-copy" style="margin-right: 10px"></i>
        产品中心合同评审申请
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
      size="medium"
    >
      <el-card class="marginTopCard box-card" shadow="never">
        <div slot="header" class="clearfix item">
          <span
            ><i class="el-icon-s-order" style="margin-right: 20px"
              ><b>合同信息</b></i
            ></span
          >
        </div>
        <el-row>
          <el-col :span="12">
            <el-form-item class="leftItem" label="申请人">
              <el-input
                :disabled="true"
                class="uniflItem"
                v-model="formItem.createUserName"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item class="rightItem" label="申请部门">
              <el-input
                :disabled="true"
                class="uniflItem"
                v-model="formItem.createDeptName"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item class="leftItem" label="委托单位" prop="entrustUnit">
              <el-input
                :disabled="true"
                class="uniflItem"
                v-model="formItem.entrustUnit"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item class="rightItem" label="单位类型" prop="typeUnit">
              <el-select
                :disabled="true"
                class="uniflItem"
                v-model="formItem.typeUnit"
                placeholder="请选择"
              >
                <el-option
                  v-for="item in formSelect.unitType"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- -- -->
        <el-row>
          <el-col :span="12">
            <el-form-item class="leftItem" label="使用单位" prop="useUnit">
              <el-input
                :disabled="true"
                class="uniflItem"
                v-model="formItem.useUnit"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              class="rightItem"
              label="产品类别"
              prop="productCategory"
            >
              <el-select
                :disabled="true"
                class="uniflItem"
                v-model="formItem.productCategory"
                placeholder="请选择"
              >
                <el-option
                  v-for="item in formSelect.deviceType"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- -- -->
        <el-row>
          <el-col :span="12">
            <el-form-item class="leftItem" label="产品名称" prop="productName">
              <el-input
                :disabled="true"
                class="uniflItem"
                v-model="formItem.productName"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item class="rightItem" label="产品类型" prop="productType">
              <el-select
                :disabled="true"
                class="uniflItem"
                v-model="formItem.productType"
                placeholder="请选择"
              >
                <el-option
                  v-for="item in formSelect.checkType"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- -- -->
        <el-row>
          <el-col :span="12">
            <el-form-item class="leftItem" label="产品数量" prop="productCount">
              <el-input
                class="uniflItem"
                v-model.number="formItem.productCount"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              class="rightItem"
              label="合同金额"
              prop="contractMoney"
            >
              <el-input
                :disabled="true"
                class="uniflItem"
                v-model="formItem.contractMoney"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- -- -->
        <el-row>
          <el-col :span="12">
            <el-form-item class="leftItem" label="实收款" prop="realMoney">
              <el-input
                :disabled="true"
                class="uniflItem"
                v-model="formItem.realMoney"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item class="rightItem" label="缴费类型" prop="paymentType">
              <el-select
                :disabled="true"
                class="uniflItem"
                v-model="formItem.paymentType"
                placeholder="请选择"
              >
                <el-option
                  v-for="item in formSelect.paymentType"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- -- -->
        <el-row>
          <el-col :span="12">
            <el-form-item class="leftItem" label="联系人" prop="liaisons">
              <el-input
                :disabled="true"
                class="uniflItem"
                v-model="formItem.liaisons"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item class="rightItem" label="联系电话" prop="phone">
              <el-input
                :disabled="true"
                class="uniflItem"
                v-model="formItem.phone"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- -- -->
        <el-row>
          <el-col :span="12">
            <el-form-item class="leftItem" label="单位地址" prop="addressUnit">
              <el-input
                :disabled="true"
                class="uniflItem"
                v-model="formItem.addressUnit"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item class="rightItem" label="邮编" prop="zipCode">
              <el-input
                :disabled="true"
                class="uniflItem"
                v-model="formItem.zipCode"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- -- -->
        <el-row>
          <el-col :span="12">
            <el-form-item class="leftItem" label="检验部门" prop="checkDept">
              <el-select
                :disabled="true"
                class="uniflItem"
                v-model="formItem.checkDept"
                placeholder="请选择"
              >
                <el-option
                  v-for="item in formSelect.checkDept"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item class="rightItem" label="编制日期">
              <div class="uniflItem">
                <el-date-picker
                  :disabled="true"
                  v-model="formItem.formationTime"
                  format="yyyy 年 MM 月 dd 日"
                >
                </el-date-picker>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- -- 可以接收 Date 类型的 JavaType-->
        <el-row>
          <el-col :span="12">
            <el-form-item class="leftItem" label="约检日期">
              <div class="uniflItem dateItem">
                <el-date-picker
                  :disabled="true"
                  v-model="formItem.orderTime"
                  format="yyyy 年 MM 月 dd 日"
                >
                </el-date-picker>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <el-card class="marginTopCard box-card" shadow="never">
        <div slot="header" class="clearfix item">
          <span
            ><i class="el-icon-s-order" style="margin-right: 20px"
              ><b>评审信息</b></i
            ></span
          >
        </div>
        <el-row>
          <el-col :xl="24" :xs="24">
            <el-form-item class="textareaStyle" label="部门负责人审核">
              <el-input
                :disabled="reviewDisable.advice_bmfzrsh"
                class="intputTextarea"
                type="textarea"
                v-model="adviceMsg.advice_bmfzrsh"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xl="24" :xs="24">
            <el-form-item class="textareaStyle" label="分管领导审核">
              <el-input
                :disabled="reviewDisable.advice_fgldsh"
                class="intputTextarea"
                type="textarea"
                v-model="adviceMsg.advice_fgldsh"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>
    </el-form>
  </div>
</template>

<script>
import Rules from "@/views/core/validArray";
import { getCurrentUserInfo } from "@/api/system/user";
import { getFormInfo, formSave } from "@/api/business/proContReview";
import {
  businessFileUpload,
  getFileListByIds,
  checkFile,
} from "@/api/business/fileModule";
import { getFormPrivilege } from "@/api/formPrivilege";
import { getAdviceMsg } from "@/api/process/process";
import { getCurrentTime } from "@/utils/proUtils";

export default {
  name: "requestCotractReview",

  data() {
    return {
      isAuthority: 0, //是否开启权限，start页面不用开启
      fileUploadBook: false,
      fileUploadAttachment: false,
      reviewDisable: {
        advice_bmfzrsh: true, //部门负责人审核
        advice_fgldsh: true, //分管领导审核
      },
      adviceMsg: {
        advice_bmfzrsh: "",
        advice_fgldsh: "",
      },
      // 作业指导书 , 附件
      fileList: [],
      attachmentList: [],
      // 下一步流程
      nextProcess: {
        id: "2",
        process: "综合评审",
      },
      // 下一步参与者弹框
      selectPersonId: "",
      tableLoading: true,
      user: {
        userId: "",
        userName: "",
        nickName: "",
        deptId: "",
        deptName: "",
      },
      formRules: {
        entrustUnit: [Rules.validArray.requireValue],
        typeUnit: [Rules.validArray.requireValueSelect],
        useUnit: [Rules.validArray.requireValue],
        productCategory: [Rules.validArray.requireValueSelect],
        productName: [Rules.validArray.requireValue],
        productType: [Rules.validArray.requireValueSelect],
        productCount: [
          Rules.validArray.requireValue,
          Rules.validArray.numberValue,
        ],
        contractMoney: [Rules.validArray.requireValue],
        realMoney: [Rules.validArray.requireValue],
        paymentType: [Rules.validArray.requireValueSelect],
        realMoney: [Rules.validArray.requireValue],
        liaisons: [Rules.validArray.requireValue],
        phone: [
          Rules.validArray.requireValue,
          Rules.validArray.inputPhoneNumberLength,
        ],
        addressUnit: [Rules.validArray.requireValue],
        // 邮编
        zipCode: [Rules.validArray.inputZipCodeLength],
        checkDept: [Rules.validArray.requireValueSelect],
      },
      // 表单属性
      formItem: {
        rowId: "", //
        entrustUnit: "", //委托单位
        typeUnit: "", //单位类型
        useUnit: "", //使用单位
        addressUnit: "", //单位地址
        productCategory: "", //产品类别
        productName: "", //产品名称
        productType: "", //产品类型
        productCount: 0, //产品数量
        contractMoney: "", //合同金额
        realMoney: "", //实收款
        paymentType: "", //缴费类型
        liaisons: "", //联系人
        phone: "", //联系电话
        zipCode: "", //邮编
        checkDept: "", //检验部门
        formationTime: "", //编制日期
        orderTime: "", //约俭日期
        // 自底向上组件传递ids
        attachmentResultIds: "",
      },
      formIsSuccess: false,
      formItemOther: {},
      // 表单中的下拉列表
      formSelect: {
        unitType: [
          {
            value: "1",
            label: "国家",
          },
          {
            value: "2",
            label: "企业",
          },
          {
            value: "3",
            label: "个人",
          },
        ],
        deviceType: [
          {
            value: "1",
            label: "AOO",
          },
          {
            value: "2",
            label: "BOO",
          },
          {
            value: "3",
            label: "COO",
          },
        ],
        checkType: [
          {
            value: "1",
            label: "High",
          },
          {
            value: "2",
            label: "Mid",
          },
          {
            value: "3",
            label: "Low",
          },
        ],
        paymentType: [
          {
            value: "1",
            label: "TopSit",
          },
          {
            value: "2",
            label: "DownSit",
          },
        ],
        checkDept: [
          {
            value: "1",
            label: "综合部",
          },
          {
            value: "2",
            label: "销售部",
          },
        ],
      },
      formResult: {},
      goParam: {},
      taskId: "",
    };
  },
  //---- methods
  methods: {
    // 作业指导书文件上传
    fileUpLoad() {
      let formData = new FormData();
      formData.append("uploadWork", this.fileList[0]);
      businessFileUpload(formData).then((response) => {
        console.log(response);
      });
    },
    attachmentFileUpload() {
      console.log("sad");
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
    returnValidValue() {
      let flag = false;
      this.$refs["formItem"].validate((valid) => {
        if (!valid) {
          this.$message.error("请检查填写数据是否有误!...");
        }
        this.formIsSuccess = valid;
      });
    },
    getCreateUserInfo() {
      getCurrentUserInfo().then((response) => {
        this.user = response.data;
      });
    },
    // 文件上传 --------
    submitUpload(type) {
      this.$refs[type].submit();
      // console.log(type);
    },
    handleRemove(file) {
      this.$message.warning("您删除了选中文件:" + file.name);
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${file.name}？`);
    },
    handleExceed() {
      this.$message.error("只能选择上传一份文件!!");
    },
    handleChangeBook(file, fileList) {
      this.fileList = fileList.slice(0);
      // console.log("changeBookFile" + this.fileList[0].name);
    },
    handleChangeAttachment(file, fileList) {
      this.attachmentList = fileList.slice(0);
      // console.log("changeAttachment" + this.attachmentList[0].name);
    },
    uploadFileBookSuccess() {
      this.fileUploadBook = true;
      this.$message.success("提交成功");
      this.fileList = [];
    },
    uploadFileAttachmentSuccess() {
      this.fileUploadAttachment = true;
      this.$message.success("提交成功");
      this.attachmentList = [];
    },

    // 表单提交 -------------
    submitForm() {
      var subData = {
        entity: this.formItem,
      };
      var adviceList = [];
      for (var item in this.formItemOther) {
        subData[item] = this.formItemOther[item];
      }
      for (var advice in this.adviceMsg) {
        subData[advice] = this.adviceMsg[advice];
      }
      formSave(subData).then((response) => {
        if (response.code === 200) {
          this.formResult = response;
        }
      });
    },
    getFormRowId() {
      var keyVal = this.formData.split("&");
      for (var i = 0; i < keyVal.length; i++) {
        var temp = keyVal[i].split("=");
        this.goParam[temp[0]] = temp[1];
      }
      this.formItem.rowId = this.goParam.rowId;
      this.taskId = this.goParam.taskId;
      this.getFormInfo();
    },
    getFormInfo() {
      getFormInfo(this.formItem.rowId).then((response) => {
        if (response.code === 200) {
          this.formItem = response.data;
          this.getFormPrivilege();
          this.$emit("previous-step-ids", this.formItem.attachmentResultIds);
        }
      });
    },
    getFormPrivilege() {
      getFormPrivilege({ taskId: this.taskId }).then((response) => {
        if (response.code === 200) {
          for (var name in response.data) {
            if (response.data[name] == "W") {
              this.reviewDisable[name] = false;
            }
          }
        }
      });
    },
    getAdviceMsg() {
      for (var name in this.adviceMsg) {
        getAdviceMsg({
          tableId: this.formItem.rowId,
          signControlName: name.split("_")[1],
        }).then((response) => {
          if (response.code === 200) {
            for (var i = 0; i < response.data.length; i++) {
              var item = response.data[i];
              this.adviceMsg["advice_" + item.controlName] = item.optionContext;
            }
          }
        });
      }
    },
  },
  watch: {
    processReturnData(newData, oldName) {
      this.formItemOther = newData;
      this.submitForm();
    },
    formResult(newData, oldName) {
      this.$emit("form-submit-return", newData);
    },
    attachmentIds(newData, oldData) {
      this.formItem.attachmentResultIds = newData;
      // console.log('update页面的 本次上传文件的 ids -- >' + newData);
      // console.log('update 页面的 本次附件ids --- >' + this.formItem.attachmentResultIds);
    },
  },
  //---- mounted
  mounted() {
    this.getFormRowId();
    this.getAdviceMsg();
  },
  props: ["formData", "processReturnData", "attachmentIds"],
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
