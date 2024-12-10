<template>
  <div>
    <el-form
      ref="formItem"
      :model="formItem"
      :rules="formRules"
      label-suffix=":"
      label-width="150px"
      label-position="right"
      size="medium"
    >
      <div style="margin-bottom: 10px">
        <i class="el-icon-s-order"></i>默认提交
        <el-tag type="info"
          ><i class="el-icon-question"></i
          >本人当前任务环节已办理完毕，需进入流程图下一办理环节或反馈本人办理意见给原任务提交者。</el-tag
        >
      </div>
      <el-row>
        <!-- v-for="(procTrans, i) in processData.procTransList" -->
        <el-col :span="15">
          <el-form-item
            style="margin-left: 50px"
            class="textareaStyle"
            label="选择下一步办理"
            prop="desc"
          >
            <el-radio-group v-if="processData != null" v-model="radio">
              <div
                v-for="(procTrans, i) in processData.procTransList"
                :key="i"
                style="float: left"
              >
                <div
                  v-if="
                    procTrans.nextActName != '新增任务' &&
                    procTrans.nextActName != '任务交办/转办'
                  "
                  style="float: left"
                >
                  <el-radio
                    name="chooseFlow"
                    style="margin-left: 20px"
                    :label="procTrans.nextActDefId"
                    :flow-name="1"
                    :processDefId="procTrans.procDefId"
                    piid=""
                    @change="changeRadio"
                    >{{ procTrans.nextActName }}</el-radio
                  >
                </div>
                <div
                  v-if="procTrans.nextActName != '结束步骤'"
                  style="float: left"
                >
                  <div
                    v-if="
                      procTrans.nextActName.includes('（加签）') ||
                      procTrans.nextActName === '任务交办/转办'
                    "
                  ></div>
                  <div
                    v-else-if="procTrans.nextActName === '完成新增任务'"
                    style="float: left"
                  >
                    <input
                      type="hidden"
                      :name="procTrans.nextActDefId + '.name'"
                      :value="procTrans.step[0].username"
                    />
                    <input
                      type="hidden"
                      :name="procTrans.nextActDefId + '.pid'"
                      :value="procTrans.step[0].userId"
                    />
                  </div>
                </div>
              </div>
            </el-radio-group>
          </el-form-item>
        </el-col>

        <div v-if="isChoose">
          <el-col :span="7">
            <el-form-item label="下一步参与者" prop="desc">
              <el-input
                :disabled="true"
                style="width: 80%"
                class="intputTextareaRules"
                type="text"
                v-model="formItem.selectUserName"
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="2">
            <el-button
              type="primary"
              size="small"
              style="width: 80px"
              @click="centerDialogVisible = true"
              >选择人员</el-button
            >
          </el-col>
        </div>
      </el-row>
    </el-form>

    <!-- 选择人员弹出框 -->
    <!-- 添加或修改角色配置对话框 -->
    <el-dialog
      title="选择人员"
      :visible.sync="centerDialogVisible"
      width="1000px"
      append-to-body
      center
      :show-close="false"
      :close-on-click-modal="false"
    >
      <el-transfer
        filterable
        :titles="['人员列表', '已添加人员']"
        :button-texts="['删除', '添加']"
        filter-placeholder="请输入人员名称"
        v-model="hasSelectPerson"
        :data="selectPerson"
      >
        <span slot-scope="{ option }">
          <el-tooltip class="item" effect="light" placement="right">
            <div slot="content">
              {{
                option.executeContent == null || option.executeContent == ""
                  ? "暂无"
                  : option.executeContent
              }}
            </div>
            <el-tag style="width: 200px; text-align: center" effect="plain">{{
              option.label
            }}</el-tag>
          </el-tooltip>
        </span>
      </el-transfer>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handelSave">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { startProcess, firstRunProcess } from "@/api/process/process";

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
  data() {
    return {
      processParam: {},
      // 下一步流程
      nextProcess: {
        id: "2",
        process: "综合评审",
      },
      // 下一步参与者弹框
      centerDialogVisible: false,
      selectPersonParam: {
        id: "",
        name: "",
      },
      tableLoading: true,
      selectPerson: [],
      hasSelectPerson: [],
      selectPersonMap: undefined,
      selectPersonUserId: "",
      // 表单属性
      formItem: {
        selectUserName: "",
        checkDate: "",
      },
      // 表单校验
      formRules: {
        test: [validArray.requireValue, validArray.inputChartLengthBetween],
        checkDate: [validArray.dateValue],
      },
      processObj: undefined,
      processData: undefined,
      radio: "",
      data: {},
      isChoose: true,
    };
  },
  props: ["processJson"],
  methods: {
    startProcessFun() {
      startProcess({
        businessKey: this.processObj.businessKey,
        processKey: this.processObj.processKey,
        ssot: this.processObj.ssot,
        reportType: this.processObj.reportType,
      }).then((response) => {
        this.processData = response.data;
        console.log("办理流程数据: " + this.processData);
        var list = this.processData.procTransList;
        this.radio = list[0].nextActDefId;
        this.selectPersonMap = new Map();
        for (var i = 0; i < list.length; i++) {
          var step = list[i].step;
          var usersList = [];
          for (var j = 0; j < step.length; j++) {
            usersList.push({
              key: step[j].userName + "_" + step[j].userId,
              label: step[j].userName,
              executeContent: step[j].executeContent,
            });
          }
          //this.selectPerson.push({procName: list[i].nextActDefId, users: {id: step[0].userId, name: step[0].userName}})
          this.selectPersonMap.set(list[i].nextActDefId, usersList);
        }
        this.handleClickRadio(this.radio);
      });
    },
    handleClickRadio(actName) {
      this.selectPerson = [];
      var userList = this.selectPersonMap.get(actName);
      for (var i = 0; i < userList.length; i++) {
        this.selectPerson.push(userList[i]);
      }
    },
    changeRadio() {
      this.isChoose = this.radio !== "jieshu";
      this.handleClickRadio(this.radio);
      this.selectPersonUserId = "";
      this.formItem.selectUserName = "";
      this.reset();
    },
    handelSave() {
      if (this.hasSelectPerson.length > 1) {
        this.$message.error("只能选择一个参与人员");
        this.centerDialogVisible = false;
        this.hasSelectPerson = [];
        this.formItem.selectUserName = "";
        return;
      }
      this.formItem.selectUserName = "";
      console.log(this.hasSelectPerson);
      for (var i = 0; i < this.hasSelectPerson.length; i++) {
        var tempArr = this.hasSelectPerson[i].split("_");
        this.formItem.selectUserName += tempArr[0] + ",";
        this.selectPersonUserId += tempArr[1] + ",";
      }
      if (this.hasSelectPerson.length == 0) {
        this.formItem.selectUserName = "";
        this.selectPersonUserId = "";
      } else {
        this.formItem.selectUserName = this.formItem.selectUserName.substring(
          0,
          this.formItem.selectUserName.length - 1
        );
        this.selectPersonUserId = this.selectPersonUserId.substring(
          0,
          this.selectPersonUserId.length - 1
        );
      }
      this.centerDialogVisible = false;
    },
    // 表单重置
    reset() {
      this.hasSelectPerson = [];
    },
    // 取消按钮
    cancel() {
      this.centerDialogVisible = false;
      this.reset();
    },
    /* getFooterData() {
        var submitData = {};
        submitData = {
          businessKey: this.processData.businessKey,
          processKey: this.processData.processKey,
          chooseFlow: this.radio,
          selectedUserIds: this.selectPersonUserId
        }
        return JSON.stringify(submitData)
      }, */

    submitProcessData() {
      if (this.hasSelectPerson != null && this.hasSelectPerson.length != 0) {
        return new Promise((resolve, reject) => {
          firstRunProcess({
            businessKey: this.processData.businessKey,
            processKey: this.processData.processKey,
            chooseFlow: this.radio,
            selectedUserIds: this.selectPersonUserId,
          }).then((response) => {
            //return response
            resolve(response);
          });
        });
      } else {
        this.$message.error("您还未选择人员！");
        return;
      }
    },
  },
  mounted: function () {
    this.$watch("processJson", (newVal, oldVal) => {
      this.processObj = JSON.parse(newVal);
      this.startProcessFun();
    });
  },
};
</script>

<style>
.intputTextarea {
  margin-left: 20px;
}

.intputTextareaRules {
  margin-left: 20px;
}
</style>
