<template>
  <div>
    <el-card class="box-card">
      <div class="text item">
        <i class="el-icon-document-copy" style="margin-right: 10px"></i>
        委托业务合同评审申请
      </div>
    </el-card>

    <div style="margin: 20px"></div>
    <el-form
      ref="formItem"
      :model="formItem"
      label-suffix=":"
      :rules="formRules"
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
            <el-form-item
              class="leftItem"
              label="设备类别"
              prop="deviceCategory"
            >
              <el-select
                :disabled="true"
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
            <el-form-item
              class="rightItem"
              label="合同编号"
              prop="contractNumber"
            >
              <el-input
                :disabled="true"
                class="uniflItem"
                v-model="formItem.contractNumber"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item class="leftItem" label="委托单位" prop="entrustUnit">
              <el-input
                :disabled="judgeIsUpdate"
                class="uniflItem"
                v-model="formItem.entrustUnit"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item class="rightItem" label="单位类型" prop="typeUnit">
              <el-select
                :disabled="judgeIsUpdate"
                class="uniflItem"
                v-model="formItem.typeUnit"
                placeholder="请选择"
              >
                <el-option
                  v-for="dict in unitType"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
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
                :disabled="judgeIsUpdate"
                class="uniflItem"
                v-model="formItem.useUnit"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item class="rightItem" label="检验部门" prop="checkDept">
              <treeselect
                :disabled="judgeIsUpdate"
                class="uniflItem"
                style="text-align: center"
                v-model="formItem.checkDept"
                :options="checkDept"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <!-- -- -->
        <el-row>
          <el-col :span="12">
            <el-form-item class="leftItem" label="设备名称" prop="deviceName">
              <el-input
                :disabled="judgeIsUpdate"
                class="uniflItem"
                v-model="formItem.deviceName"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              class="rightItem"
              label="检验类型"
              prop="checkCategory"
            >
              <el-select
                :disabled="judgeIsUpdate"
                class="uniflItem"
                v-model="formItem.checkCategory"
                placeholder="请选择"
              >
                <el-option
                  v-for="dict in checkType"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- -- -->
        <el-row>
          <el-col :span="12">
            <el-form-item class="leftItem" label="设备数量" prop="deviceCount">
              <el-input
                :disabled="judgeIsUpdate"
                class="uniflItem"
                v-model.number="formItem.deviceCount"
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
                :disabled="judgeIsUpdate"
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
                :disabled="judgeIsUpdate"
                class="uniflItem"
                v-model="formItem.realMoney"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item class="rightItem" label="缴费类型" prop="paymentType">
              <el-select
                :disabled="judgeIsUpdate"
                class="uniflItem"
                v-model="formItem.paymentType"
                placeholder="请选择"
              >
                <el-option
                  v-for="dict in paymentType"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
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
                :disabled="judgeIsUpdate"
                class="uniflItem"
                v-model="formItem.liaisons"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item class="rightItem" label="联系电话" prop="phone">
              <el-input
                :disabled="judgeIsUpdate"
                class="uniflItem"
                maxlength="11"
                show-word-limit
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
                :disabled="judgeIsUpdate"
                class="uniflItem"
                v-model="formItem.addressUnit"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item class="rightItem" label="邮编" prop="zipCode">
              <el-input
                :disabled="judgeIsUpdate"
                class="uniflItem"
                v-model="formItem.zipCode"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- -- -->
        <el-row>
          <!-- -- 可以接收 Date 类型的 JavaType-->
          <el-col :span="12">
            <el-form-item class="leftItem" label="约检日期" prop="orderTime">
              <el-date-picker
                :disabled="judgeIsUpdate"
                class="uniflItem"
                v-model="formItem.orderTime"
                type="date"
                format="yyyy 年 MM 月 dd 日"
                :picker-options="pickerOptions"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              class="rightItem"
              label="编制日期"
              prop="formationTime"
            >
              <el-date-picker
                :disabled="judgeIsUpdate"
                class="uniflItem"
                v-model="formItem.formationTime"
                type="date"
                format="yyyy 年 MM 月 dd 日"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <!-- joinValues -->
            <el-form-item
              class="leftItem"
              label="作业指导书"
              prop="fileResultIds"
            >
              <treeselect
                class="uniflItem"
                style="text-align: 10px; width: 780px; text-align: left"
                v-model="formItem.fileResultIdArr"
                searchable
                disableBranchNodes
                openDirection="top"
                :options="fileWorks"
                :show-count="true"
                :multiple="true"
                joinValues
                :disabled="judgeIsUpdate"
                placeholder="请选择作业指导书"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- <el-row v-if="isEndStep">
          <el-col :span="12">
            <el-form-item class="leftItem" label="检验员" prop="checkUserName">
              <el-input :disabled="true" class="uniflItem " v-model="formItem.checkUserName">
                <i @click="clickSelectPeopleDialog" slot="suffix" class="el-input__icon el-icon-search" style="cursor: pointer;"></i>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row> -->
      </el-card>

      <DeviceList
        ref="deviceComponent"
        v-if="isEndStep"
        :deviceList="deviceList"
      ></DeviceList>

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
                @dblclick.native="showDefaultOpinions('advice_bmfzrsh')"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xl="24" :xs="24">
            <el-form-item class="textareaStyle" label="业务部审核">
              <el-input
                :disabled="reviewDisable.advice_ywbsh"
                class="intputTextarea"
                type="textarea"
                v-model="adviceMsg.advice_ywbsh"
                @dblclick.native="showDefaultOpinions('advice_ywbsh')"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xl="24" :xs="24">
            <el-form-item class="textareaStyle" label="财务部审核">
              <el-input
                :disabled="reviewDisable.advice_cwbsh"
                class="intputTextarea"
                type="textarea"
                v-model="adviceMsg.advice_cwbsh"
                @dblclick.native="showDefaultOpinions('advice_cwbsh')"
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
                @dblclick.native="showDefaultOpinions('advice_fgldsh')"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>

      <AdviceWord
        ref="adviceWord"
        v-on:setQuickOpinion="setQuickOpinion"
      ></AdviceWord>
    </el-form>

    <!-- <el-dialog title="请选择检验员" :visible.sync="selectPeopleDialog">
      <SelectPeople ref="selectPeople"></SelectPeople>
      <div slot="footer" class="dialog-footer" style="margin-top: 15px; text-align: center;">
        <el-button type="primary" @click="handleSaveDialog">确 定</el-button>
        <el-button @click="cancelDialog">取 消</el-button>
      </div>
    </el-dialog> -->
  </div>
</template>

<script>
import { getFormInfo, formSave } from "@/api/business/reqContReview";
import { getCurrentUserInfo } from "@/api/system/user";
import { getFormPrivilege } from "@/api/formPrivilege";
import { getAdviceMsg } from "@/api/process/process";
import { getDicByType } from "@/api/system/dict/data";
import { treeselectFile } from "@/api/business/fileWork";
import { saveDeviceList } from "@/api/business/contractDevice";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { treeselect } from "@/api/system/dept";
import AdviceWord from "@/views/common/adviceWord/index";
import Treeselect from "@riophae/vue-treeselect";
import DeviceList from "@/views/common/device/index";
import SelectPeople from "@/views/components/dialog/SelectPeople";
import Rules from "@/views/core/validArray";

export const validArray = {
  // 强制条件
  requireValue: {
    required: true,
    message: "该项不能为空",
    trigger: "blur",
  },
  // 输入字符长度条件
  inputChartLengthMin: {
    min: 6,
    message: "不能低于6个字符",
    trigger: "blur",
  },
  inputChartLengthBetween: {
    min: 3,
    max: 6,
    message: "长度在 3 到 6 个字符",
    trigger: "blur",
  },
  inputChartLengthMax: {
    max: 12,
    message: "不能超过12个字符",
    trigger: "blur",
  },
  // 检验类型
  numberValue: {
    type: "number",
    message: "请输入数字",
    trigger: "blur",
  },
  dateValue: {
    type: "date",
    required: true,
    message: "请选择日期",
    trigger: "change",
  },
  timeValue: {
    type: "date",
    required: true,
    message: "请选择时间",
    trigger: "change",
  },
};
export default {
  name: "requestCotractReview",
  components: {
    Treeselect,
    AdviceWord,
    SelectPeople,
    DeviceList,
  },
  data() {
    return {
      isAuthority: 0, //是否开启权限，start页面不用开启
      fileUploadBook: false,
      fileUploadAttachment: false,
      //输入框权限
      reviewDisable: {
        advice_bmfzrsh: true, //部门负责人审核
        advice_fgldsh: true, //分管领导审核
        advice_ywbsh: true, //业务部审核
        advice_cwbsh: true,
        entrustUnit: true,
        typeUnit: true,
        useUnit: true,
        deviceCategory: true,
        deviceName: true,
        checkCategory: true,
        deviceCount: true,
        contractMoney: true,
        realMoney: true,
        paymentType: true,
        liaisons: true,
        phone: true,
        addressUnit: true,
        zipCode: true,
        checkDept: true,
        formationTime: true,
        orderTime: true,
      },
      adviceMsg: {
        advice_bmfzrsh: "",
        advice_fgldsh: "",
        advice_ywbsh: "",
        advice_cwbsh: "",
      },
      // 表单属性
      formItem: {
        rowId: "", //
        entrustUnit: "", //委托单位
        typeUnit: "", //单位类型
        useUnit: "", //使用单位
        addressUnit: "", //单位地址
        deviceCategory: "", //设备类别
        deviceName: "", //设备名称
        checkCategory: "", //检验类别
        deviceCount: "0", //设备数量
        contractMoney: "", //合同金额
        realMoney: "", //实收款
        paymentType: "", //缴费类型
        liaisons: "", //联系人
        phone: "", //联系电话
        zipCode: "", //邮编
        checkDept: "", //检验部门
        formationTime: "", //编制日期
        orderTime: "", //约俭日期
        confirmPrint: null,
        isEnd: null,
        attachmentResultIds: "",
      },
      isEndNode: false,
      formRules: {},
      fileUpdateFlag: false,
      formItemOther: {},
      // 表单中的下拉列表
      unitType: [],
      deviceType: [],
      checkType: [
        {
          value: "1",
          label: "委托检验",
        },
        {
          value: "2",
          label: "本院检验",
        },
        {
          value: "3",
          label: "其他检验",
        },
      ],
      paymentType: [],
      checkDept: [],

      pickerOptions: {
        // 日期控制 , 约检日期不能在今天前边
        disabledDate(time) {
          return time.getTime() < Date.now();
        },
      },
      formResult: {},
      goParam: {},
      taskId: "",
      judgeIsUpdate: true,
      user: {
        deptId: "",
        deptName: "",
        nickName: "",
        userId: "",
        username: "",
      },
      fileWorks: [], //作业指导书选项，
      currentAdvice: "", //当前流程步骤id，如bmfzrsh,
      //是否显示选择检验员输入框及设备列表
      isEndStep: false,
      //是否显示检验员dialog
      selectPeopleDialog: false,
      //设备列表
      deviceList: [],
      formIsSuccess: false,
    };
  },
  props: ["formData", "processReturnData", "attachmentIds"],
  // ------------------------------------------------
  created() {
    this.getUnitTypeDict().then((response) => {
      this.unitType = response.data;
    });
    this.getDeviceCateDict().then((response) => {
      this.deviceType = response.data;
    });
    this.getPaymentTypeDict().then((response) => {
      this.paymentType = response.data;
    });
    /** 查询部门下拉树结构 */
    treeselect().then((response) => {
      this.checkDept = response.data;
      // console.log(this.checkDept);
    });
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
      this.fileUpdateFlag = true;
      // console.log('update页面的 本次上传文件的 ids -- newData >' + newData);
      this.formItem.attachmentResultIds = newData;
      // console.log('current this -->' + this.formItem.attachmentResultIds);
      // console.log('update 页面的 本次附件ids --- >' + this.formItem.attachmentResultIds);
    },
    "user.nickName": {
      handler(newData, oldData) {
        // console.log(newData);
        // console.log(this.user);
        if (newData == this.formItem.createUserName) {
          // console.log('相等' + newData + '==' + this.formItem.createUserName);
          this.judgeIsUpdate = false;
        }
      },
      deep: true,
    },
  },

  //---- methods
  methods: {
    // 表单提交 ------------- 表单数据
    returnFormValue() {
      // 只有当前需要输入的有意见字段 才判断写意见了没
      if (this.currentAdvice && !this.isEndNode && !this.adviceMsg[this.currentAdvice]) {
        this.$message.error("请输入 审核意见!");
        this.formIsSuccess = false;
        return;
      }
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
    submitForm() {
      if (!this.fileUpdateFlag) {
        this.formItem.attachmentResultIds = "";
      }
      var subData = {
        entity: this.formItem,
      };
      for (var item in this.formItemOther) {
        subData[item] = this.formItemOther[item];
      }
      for (var advice in this.adviceMsg) {
        if (advice === this.currentAdvice) {
          subData[advice] = this.adviceMsg[advice];
        } else {
          subData[advice] = "";
        }
      }
      this.$refs["formItem"].validate((valid) => {
        if (valid) {
          formSave(subData).then((response) => {
            if (this.isEndStep) {
              this.$refs.deviceComponent.saveDeviceList();
            }
            this.formResult = response;
          });
        }
      });
    },
    isProcessEndNode() {
      this.isEndNode = true;
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
          this.getTreeselectFile();
          this.getFormPrivilege();
          // console.log('ids' + this.formItem.attachmentResultIds);
          this.$emit("previous-step-ids", this.formItem.attachmentResultIds);
        }
      });
    },
    getFormPrivilege() {
      getFormPrivilege({
        taskId: this.taskId,
      }).then((response) => {
        if (response.code === 200) {
          for (var name in response.data) {
            if (response.data[name] == "W") {
              this.reviewDisable[name] = false;
              if (name.startsWith("advice_")) {
                //说明是当前审批人应该输入的意见框
                this.currentAdvice = name;
              }
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
    //查询作业指导书并回显已经选择的选项
    getTreeselectFile() {
      var fileResultIds = this.formItem.fileResultIds;
      if (fileResultIds != "" && fileResultIds != null) {
        this.formItem["fileResultIdArr"] = fileResultIds.split(",");
      }
      treeselectFile().then((response) => {
        this.fileWorks = response.data;
      });
    },
    //打开快捷意见选择框
    showDefaultOpinions(type) {
      if (this.reviewDisable[type] == false) {
        this.$refs.adviceWord.changeDialog(true);
      }
    },
    //根据快捷意见选择框选择的值设置当前流程意见输入框
    setQuickOpinion(opinion) {
      this.adviceMsg[this.currentAdvice] = opinion;
    },

    //检验员选择
    clickSelectPeopleDialog() {
      this.selectPeopleDialog = true;
    },
    handleSaveDialog() {
      var selectPeopleData = this.$refs.selectPeople.getSelectPeopleByDialog();
      var ids = "";
      var names = "";
      for (var i = 0; i < selectPeopleData.length; i++) {
        var bean = selectPeopleData[i];
        ids += bean.userId + ",";
        names += bean.username + ",";
      }
      this.formItem.checkUserId = ids.substring(0, ids.length - 1);
      this.formItem.checkUserName = names.substring(0, names.length - 1);
      this.selectPeopleDialog = false;
      //this.$refs.selectPeople.resetToData();
    },
    cancelDialog() {
      this.$refs.selectPeople.resetToData();
      this.selectPeopleDialog = false;
    },
    showPeopleAndDialog(flag) {
      this.isEndStep = flag;
      if (flag) {
        this.formRules["checkUserName"] = [Rules.validArray.requireValue];
        this.initDeviceList();
      }
    },
    initDeviceList() {
      this.isShowDeviceList = true;
      getDicByType(this.formItem.deviceCategory).then((response) => {
        var dict = response.data;
        var deviceListTemp = [];

        for (var i = 0; i < this.formItem.deviceCount; i++) {
          deviceListTemp.push({
            orderNum: i + 1,
            deviceNumber: this.formItem.contractNumber + "-BG-" + (i + 1),
            deviceName: dict.dictLabel,
            useUnit: this.formItem.useUnit,
            deviceCategory: this.formItem.deviceCategory,
            contractId: this.formItem.rowId,
          });
        }
        this.deviceList = deviceListTemp;
      });
    },
  },
  //---- mounted
  mounted() {
    this.getFormRowId();
    this.getCreateUserInfo();
    this.getAdviceMsg();
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
