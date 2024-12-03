<template>
  <div>
    <el-card class="box-card">
      <div class="text item">
        <i class="el-icon-document-copy" style="margin-right: 10px;"></i>
        委托业务合同评审申请
      </div>
    </el-card>

    <div style="margin: 20px;"></div>
    <el-form ref="formItem" :model="formItem" label-suffix=":" label-width="150px" label-position="right" size="medium">
      <el-card class="marginTopCard box-card" shadow="never">
        <div slot="header" class="clearfix item">
          <span><i class="el-icon-s-order" style="margin-right: 20px;"><b>合同信息</b></i></span>
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
            <el-form-item class="leftItem" label="设备类别" prop="deviceCategory">
                <el-select :disabled="true" class="uniflItem" v-model="formItem.deviceCategory" placeholder="请选择">
                  <el-option
                    v-for="dict in deviceType"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue">
                  </el-option>
                </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
              <el-form-item class="rightItem" label="合同编号" prop="contractNumber">
                <el-input :disabled="true" class="uniflItem" v-model="formItem.contractNumber"></el-input>
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
            <el-form-item class="rightItem" label="单位类型" prop="typeUnit">
                <el-select :disabled="true"  class="uniflItem" v-model="formItem.typeUnit" placeholder="请选择">
                  <el-option
                    v-for="dict in unitType"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue">
                  </el-option>
                </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- -- -->
        <el-row>
            <el-col :span="12">
              <el-form-item class="leftItem" label="使用单位" prop="useUnit">
                  <el-input :disabled="true" class="uniflItem " v-model="formItem.useUnit"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="rightItem" label="检验部门" prop="checkDept">
                <treeselect :disabled="true" class="uniflItem" style="text-align: center;" v-model="formItem.checkDept" :options="checkDept"/>
              </el-form-item>
            </el-col>
          </el-row>
        <!-- -- -->
        <el-row>
          <el-col :span="12">
            <el-form-item class="leftItem" label="设备名称" prop="deviceName">
                <el-input :disabled="true" class="uniflItem " v-model="formItem.deviceName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item class="rightItem" label="检验类型" prop="checkCategory">
                <el-select :disabled="true" class="uniflItem" v-model="formItem.checkCategory" placeholder="请选择">
                    <el-option
                      v-for="dict in checkType"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value">
                    </el-option>
                </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- -- -->
        <el-row>
          <el-col :span="12">
            <el-form-item class="leftItem" label="设备数量" prop="deviceCount">
                <el-input :disabled="true" class="uniflItem " v-model.number="formItem.deviceCount"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item class="rightItem" label="合同金额" prop="contractMoney">
                <el-input :disabled="true" class="uniflItem " v-model="formItem.contractMoney"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- -- -->
        <el-row>
          <el-col :span="12">
            <el-form-item class="leftItem" label="实收款" prop="realMoney">
                <el-input :disabled="true" class="uniflItem " v-model="formItem.realMoney"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item class="rightItem" label="缴费类型" prop="paymentType">
                <el-select :disabled="true" class="uniflItem" v-model="formItem.paymentType" placeholder="请选择">
                  <el-option
                    v-for="dict in paymentType"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue">
                  </el-option>
                </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- -- -->
        <el-row>
          <el-col :span="12">
            <el-form-item class="leftItem" label="联系人" prop="liaisons">
                <el-input :disabled="true" class="uniflItem " v-model="formItem.liaisons"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item class="rightItem" label="联系电话" prop="phone">
                <el-input :disabled="true" class="uniflItem"  maxlength="11" show-word-limit v-model="formItem.phone"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- -- -->
        <el-row>
          <el-col :span="12">
            <el-form-item class="leftItem" label="单位地址" prop="addressUnit">
                <el-input :disabled="true" class="uniflItem " v-model="formItem.addressUnit"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item class="rightItem" label="邮编" prop="zipCode">
                <el-input :disabled="true" class="uniflItem " v-model="formItem.zipCode"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- -- -->
        <el-row>
        <!-- -- 可以接收 Date 类型的 JavaType-->
          <el-col :span="12">
            <el-form-item class="leftItem" label="约检日期" prop="orderTime">
                  <el-date-picker
                  :disabled="true"
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
            <el-form-item class="rightItem" label="编制日期" prop="formationTime">
                  <el-date-picker
                  :disabled="true"
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
            <el-col :span="24"><!-- joinValues -->
              <el-form-item class="leftItem" label="作业指导书" prop="fileResultIds">
                  <treeselect  class="uniflItem" style="text-align: 10px;width: 780px;text-align: left;"
                              v-model="formItem.fileResultIdArr"
                              searchable
                              disableBranchNodes
                              openDirection="top"
                              :options="fileWorks"
                              :show-count="true"
                              :multiple="true"
                              joinValues
                              :disabled="true"
                              placeholder="请选择作业指导书"/>
              </el-form-item>
            </el-col>
          </el-row>

		  <el-row v-if="formItem.checkUserName != null || formItem.checkUserName != ''">
		    <el-col :span="12">
		      <el-form-item class="leftItem" label="检验员" prop="checkUserName">
		        <el-input :disabled="true" class="uniflItem " v-model="formItem.checkUserName">
		        </el-input>
		      </el-form-item>
		    </el-col>
		  </el-row>
      </el-card>

      <!-- 设备列表，只在最后环节显示此列表-->
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
                        v-model="adviceMsg.advice_bmfzrsh"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xl="24" :xs="24">
            <el-form-item class="textareaStyle" label="业务部审核">
              <el-input :disabled="reviewDisable.advice_ywbsh" 
                        class="intputTextarea" type="textarea" 
                        v-model="adviceMsg.advice_ywbsh"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :xl="24" :xs="24">
            <el-form-item class="textareaStyle" label="分管领导审核" >
              <el-input :disabled="reviewDisable.advice_fgldsh" class="intputTextarea" type="textarea"
                        v-model="adviceMsg.advice_fgldsh"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>
    </el-form>
  </div>

</template>

<script>
  import { getFormInfo, formSave } from '@/api/business/reqContReview'
  import { getCurrentUserInfo  } from "@/api/system/user";
  import { getFormPrivilege } from "@/api/formPrivilege";
  import { getAdviceMsg } from "@/api/process/process";
  import { getAllDeviceList } from '@/api/business/contractDevice'
  import { treeselectFile } from "@/api/business/fileWork";
  import DeviceList from '@/views/common/device/index'
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  import { treeselect } from "@/api/system/dept";


  export const validArray = {
    // 强制条件
    requireValue: {
      required: true,
      message: '该项不能为空',
      trigger: 'blur'
    },
    // 输入字符长度条件
    inputChartLengthMin: {
      min: 6,
      message: '不能低于6个字符',
      trigger: 'blur'
    },
    inputChartLengthBetween: {
      min: 3,
      max: 6,
      message: '长度在 3 到 6 个字符',
      trigger: 'blur'
    },
    inputChartLengthMax: {
      max: 12,
      message: '不能超过12个字符',
      trigger: 'blur'
    },
    // 检验类型
    numberValue: {
      type: 'number',
      message: '请输入数字',
      trigger: 'blur'
    },
    dateValue: {
      type: 'date',
      required: true,
      message: '请选择日期',
      trigger: 'change'
    },
    timeValue: {
      type: 'date',
      required: true,
      message: '请选择时间',
      trigger: 'change'
    }
  }
  export default {
    name: 'requestCotractReviewView',
    components: {
      DeviceList,
      Treeselect
    },
    data() {
      return {
        isAuthority: 0, //是否开启权限，start页面不用开启
        reviewDisable: {
          advice_bmfzrsh: true, //部门负责人审核
          advice_fgldsh: true ,//分管领导审核
          advice_ywbsh: true, //业务部审核
        },
        adviceMsg: {
          advice_bmfzrsh: '',
          advice_fgldsh: '',
          advice_ywbsh: '',
        },
        // 下一步流程
        nextProcess: {
          id: '2',
          process: '综合评审'
        },
        // 表单属性
        formItem: {
          rowId: '', //
          entrustUnit: '', //委托单位
          typeUnit: '', //单位类型
          useUnit: '', //使用单位
          addressUnit: '', //单位地址
          deviceCategory: '', //设备类别
          deviceName: '', //设备名称
          checkCategory: '', //检验类别
          deviceCount: "0", //设备数量
          contractMoney: '', //合同金额
          realMoney: '', //实收款
          paymentType: '', //缴费类型
          liaisons: '', //联系人
          phone: '', //联系电话
          zipCode: '', //邮编
          checkDept: '', //检验部门
          formationTime: '', //编制日期
          orderTime: '', //约俭日期
          attachmentResultIds:''
        },
        formItemOther: {},

        // 表单中的下拉列表
        unitType:[],
        deviceType:[],
        checkType:[
          {value: '1',label: '委托检验'},
          {value: '2',label: '本院检验'},
          {value: '3',label: '其他检验'},
        ],
        paymentType:[],
        checkDept:[],

        pickerOptions: {
          // 日期控制 , 约检日期不能在今天前边
          disabledDate(time) {
            return time.getTime() < Date.now();
          },
        },

        formResult: {},
        goParam: {},
        taskId: '',
        isShowDeviceList: false,
        deviceList: [],
        fileWorks: [], //作业指导书选项
      };
    },
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
      treeselect().then(response => {
        this.checkDept = response.data;
        // console.log(this.checkDept);
      });
  },
    //---- methods
    methods: {
      getCreateUserInfo() {
        getCurrentUserInfo().then(response => {
          this.user = response.data
        })
      },

      getFormRowId() {
        var keyVal = this.formData.split("&");
        for(var i = 0; i < keyVal.length; i++) {
          var temp = keyVal[i].split("=");
          this.goParam[temp[0]] = temp[1]
        }
        this.formItem.rowId = this.goParam.rowId;
        this.getFormInfo();
      },
      getFormInfo() {
        getFormInfo(this.formItem.rowId).then(response => {
          if (response.code === 200) {
            this.formItem = response.data;
            this.getTreeselectFile()
            this.$emit('previous-step-ids', this.formItem.attachmentResultIds);
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
      getDeviceList() {
        getAllDeviceList(this.formItem.rowId).then(response => {
          if(response.code === 200) {
            if(response.data.length > 0) {
              this.isShowDeviceList = true;
              this.deviceList = response.data;
            }
          }
        })
      },
      //查询作业指导书并回显已经选择的选项
      getTreeselectFile() {
        var fileResultIds = this.formItem.fileResultIds;
        if(fileResultIds != '' && fileResultIds != undefined) {
          this.formItem['fileResultIdArr'] = fileResultIds.split(",");
        }
        treeselectFile().then(response => {
          this.fileWorks = response.data;
        })
      }
    },
    //---- mounted
    mounted() {
      this.getFormRowId();
      this.getCreateUserInfo();
      this.getAdviceMsg();
      this.getDeviceList();
    },
    props: ['formData'],
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
