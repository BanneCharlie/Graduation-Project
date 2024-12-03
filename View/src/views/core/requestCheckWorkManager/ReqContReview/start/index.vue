<template>
  <div>
    <el-card class="box-card">
      <div class="text item">
        <i class="el-icon-document-copy" style="margin-right: 10px;"></i>
        委托业务合同评审申请
      </div>
    </el-card>

    <div style="margin: 20px;"></div>
    <el-form ref="formItem" :model="formItem" :rules="formRules" label-suffix=":" label-width="150px" label-position="right"
      size="medium">
      <el-card class="marginTopCard box-card" shadow="never">
        <div slot="header" class="clearfix item">
          <span><i class="el-icon-s-order" style="margin-right: 20px;"><b>合同信息</b></i></span>
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
            <el-form-item class="leftItem" label="设备类别" prop="deviceCategory">
              <!-- :disabled="deviceTypeDisabled"  -->
              <el-select class="uniflItem" v-model="formItem.deviceCategory" placeholder="请选择">
                <el-option v-for="dict in deviceType" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue">
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
              <el-input class="uniflItem" v-model="formItem.entrustUnit"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item class="rightItem" label="单位类型" prop="typeUnit">
              <el-select class="uniflItem" v-model="formItem.typeUnit" placeholder="请选择">
                <el-option v-for="dict in unitType" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- -- -->
        <el-row>
          <el-col :span="12">
            <el-form-item class="leftItem" label="使用单位" prop="useUnit">
              <el-input class="uniflItem " v-model="formItem.useUnit"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item class="rightItem" label="检验部门" prop="checkDept">
              <treeselect class="uniflItem" style="text-align: 10px;" v-model="formItem.checkDept" :options="checkDept"
                :show-count="true" placeholder="请选择归属部门" />
            </el-form-item>
          </el-col>
        </el-row>
        <!-- -- -->
        <el-row>
          <el-col :span="12">
            <el-form-item class="leftItem" label="设备名称" prop="deviceName">
              <el-input class="uniflItem " v-model="formItem.deviceName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item class="rightItem" label="检验类型" prop="checkCategory">
              <el-select class="uniflItem" v-model="formItem.checkCategory" placeholder="请选择">
                <el-option v-for="dict in checkType" :key="dict.value" :label="dict.label" :value="dict.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- -- -->
        <el-row>
          <el-col :span="12">
            <el-form-item class="leftItem" label="设备数量" prop="deviceCount">
              <el-input class="uniflItem " v-model.number="formItem.deviceCount"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item class="rightItem" label="合同金额" prop="contractMoney">
              <el-input class="uniflItem " v-model="formItem.contractMoney"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- -- -->
        <el-row>
          <el-col :span="12">
            <el-form-item class="leftItem" label="实收款" prop="realMoney">
              <el-input class="uniflItem " v-model="formItem.realMoney"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item class="rightItem" label="缴费类型" prop="paymentType">
              <el-select class="uniflItem" v-model="formItem.paymentType" placeholder="请选择">
                <el-option v-for="dict in paymentType" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- -- -->
        <el-row>
          <el-col :span="12">
            <el-form-item class="leftItem" label="联系人" prop="liaisons">
              <el-input class="uniflItem " v-model="formItem.liaisons"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item class="rightItem" label="联系电话" prop="phone">
              <el-input class="uniflItem" maxlength="11" show-word-limit v-model="formItem.phone"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- -- -->
        <el-row>
          <el-col :span="12">
            <el-form-item class="leftItem" label="单位地址" prop="addressUnit">
              <el-input class="uniflItem " v-model="formItem.addressUnit"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item class="rightItem" label="邮编" prop="zipCode">
              <el-input class="uniflItem " v-model="formItem.zipCode"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- -- -->
        <el-row>
          <!-- -- 可以接收 Date 类型的 JavaType-->
          <el-col :span="12">
            <el-form-item class="leftItem" label="约检日期" prop="orderTime">
              <el-date-picker class="uniflItem" v-model="formItem.orderTime" type="date" format="yyyy 年 MM 月 dd 日"
                :picker-options="pickerOptions">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item class="rightItem" label="编制日期" prop="formationTime">
              <el-date-picker class="uniflItem" v-model="formItem.formationTime" type="date" format="yyyy 年 MM 月 dd 日">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24">
            <!-- joinValues -->
            <el-form-item class="leftItem" label="作业指导书" prop="fileResultIds">
              <treeselect class="uniflItem" style="text-align: 10px;width: 780px;text-align: left;" v-model="formItem.fileResultIds"
                searchable disableBranchNodes openDirection="top" :options="fileWorks" :show-count="true" :multiple="true"
                joinValues placeholder="请选择作业指导书" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>
    </el-form>

  </div>

</template>

<script>
  import { getCurrentUserInfo } from "@/api/system/user";
  import { formSave } from "@/api/business/reqContReview";
  import { treeselect } from "@/api/system/dept";
  import { treeselectFile } from "@/api/business/fileWork";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  import { getContractNumber, handleCurrentNumber } from '@/api/business/businessNumber';
  import { v4 } from 'uuid' // npm install -S uuid
  import FormFooter from '@/views/core/common/footer/FormFooter'
  import Rules from '@/views/core/validArray'
  import Treeselect from "@riophae/vue-treeselect";

  export default {
    name: 'requestCotractReview',
    components: {
      FormFooter,
      Treeselect,
    },
    data() {
      return {
        isAuthority: 0, //是否开启权限，start页面不用开启
        reviewDisable: {
          advice_bmfzrsh: true, //部门负责人审核
          advice_fgldsh: true ,//分管领导审核,
          advice_ywbsh: true, //业务部审核
          advice_cwbsh: true,
        },
        user: {
          userId: '',
          userName: '',
          nickName: '',
          deptId: '',
          deptName: ''
        },
        formRules: {
          entrustUnit: [Rules.validArray.requireValue],
          typeUnit: [Rules.validArray.requireValueSelect],
          useUnit: [Rules.validArray.requireValue],
          deviceCategory: [Rules.validArray.requireValueSelect],
          deviceName: [Rules.validArray.requireValue],
          checkCategory: [Rules.validArray.requireValueSelect],
          deviceCount: [Rules.validArray.requireValue, Rules.validArray.numberValue, Rules.validArray.signlessNumber],
          contractMoney: [Rules.validArray.requireValue, Rules.validArray.signlessNumber],
          realMoney: [Rules.validArray.signlessNumber],
          paymentType: [Rules.validArray.requireValueSelect],
          liaisons: [Rules.validArray.requireValue],
          phone: [Rules.validArray.requireValue, Rules.validArray.inputPhoneNumberLength],
          addressUnit: [Rules.validArray.requireValue],
          // 邮编
          zipCode: [Rules.validArray.inputZipCodeLength],
          checkDept: [Rules.validArray.requireValueSelect],
          formationTime: [Rules.validArray.requireValue, Rules.validArray.dateValue],
          orderTime: [Rules.validArray.requireValue, Rules.validArray.dateValue]
        },
        // 表单属性
        formItem: {
          deviceCategory: '',
          contractNumber: '',
          realMoney: 0 ,
          // 自底向上组件传递ids
          attachmentResultIds: this.attachmentIds
        },
        formIsSuccess: false,

        // deviceTypeDisabled:false,
        formItemOther: {

        },
        currentOperationIdentifying: '',
        number: '',
        tempNumber: '',
        oldNumber: '',
        oldDeviceCategory: '',
        // 表单中的下拉列表
        unitType: [],
        deviceType: [],
        checkType: [{
            value: '1',
            label: '委托检验'
          },
          {
            value: '2',
            label: '本院检验'
          },
          {
            value: '3',
            label: '其他检验'
          },
        ],
        paymentType: [],
        checkDept: [],
        fileWorks: [], //作业指导书选项

        pickerOptions: {
          // 日期控制 , 约检日期不能在今天前边
          disabledDate(time) {
            return time.getTime() < Date.now();
          },
        },
        formResult: {},
        //是否显示作业指导书
        selectFileWorkDialog: true,
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
      this.getTreeselect();
    },
    watch: {
      processReturnData(newData, oldData) {
        this.formItemOther = newData
        this.submitForm()
      },
      attachmentIds(newData, oldData) {
        // console.log(newData);
        this.formItem.attachmentResultIds = newData
        // console.log('---'+this.formItem.attachmentResultIds);
      },
      // 监听 设备类别的变化 然后向后端请求编号
      /*
          1. 如果是第一次进入页面时 设备进行变化 那么就直接向后台获取编号
          2. 如果是 已经选好了但是 还是要变更编号 那么就需要清理本次编号并且获取新的编号
      */
      'formItem.deviceCategory': {
        handler(newData, oldData) {
          if (this.oldDeviceCategory == '') {
            this.oldDeviceCategory = newData;
            // console.log('第一次选择设备列表' + this.oldDeviceCategory);
            this.selectDeviceTypeAroundMethod(v4());
          } else {
            this.oldDeviceCategory = oldData;
            // console.log('this number-->' + this.number);
            // 清理之前的 编号
            handleCurrentNumber(this.oldDeviceCategory, this.number).then((response) => {
              if (response.code == 200) {
                // console.log('更换设备类型，清除缓存成功--' + this.oldDeviceCategory);
                this.selectDeviceTypeAroundMethod(v4());
              }
            });
          }
        },
      },
      'formItem.contractNumber': {
        handler(newData, oldData) {
          const colseData = {
            handleNumber: this.number,
            handleDeviceCagegory: this.formItem.deviceCategory
          }
          // console.log('表单传递参数-->template data');
          // console.log(colseData);
          this.$emit('handle-change-number', colseData);
        },
      },
    },
    //---- mounted
    mounted() {
      this.getCreateUserInfo();
    },
    props: ['formData', 'processReturnData', 'attachmentIds'],
    //---- methods
    methods: {
      /*
        选择完 设备类别之后 会调用该方法
      */
      selectDeviceTypeAroundMethod(uuid) {
        const deviceType = this.formItem.deviceCategory;
        this.currentOperationIdentifying = uuid;
        getContractNumber(deviceType).then((response) => {
          this.formItem.contractNumber = response.data.contractNumber;
          this.number = response.data.number;
        });
      },
      /** 查询部门下拉树结构 */
      getTreeselect() {
        treeselect().then(response => {
          this.checkDept = response.data;
        });
        treeselectFile().then(response => {
          this.fileWorks = response.data;
        });
      },
      // 表单提交 ------------- 表单数据方法
      submitForm() {
        var subData = {
          entity: this.formItem,
          currentOperationIdentifying: this.currentOperationIdentifying,
          number: this.number,
        }
        for (var item in this.processReturnData) {
          subData[item] = this.processReturnData[item]
        }
        this.$refs['formItem'].validate((valid) => {
          if (valid) {
            formSave(subData).then(response => {
              if (response.code === 200) {
                this.$emit('form-submit-return', response)
              }
            })
          }
        });
      },
      returnFormValue() {
        this.returnValidValue();
        return new Promise((resolve, reject) => {
          const value = {
            flag: this.formIsSuccess
          }
          resolve(value);
        })
      },
      returnValidValue() {
        let flag = false;
        this.$refs['formItem'].validate((valid) => {
          if (!valid) {
            this.$message.error('请检查填写数据是否有误!...');
          }
          this.formIsSuccess = valid;
        });
      },
      getCreateUserInfo() {
        getCurrentUserInfo().then(response => {
          this.user = response.data
        })
      },
      resetForm(formName) {
        if (confirm('您确定要重置当前表单吗?')) {
          this.$refs[formName].resetFields();
        } else {
          this.$message.warning("请谨慎操作");
        }
      },
    },
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
    text-align: center !important;
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
