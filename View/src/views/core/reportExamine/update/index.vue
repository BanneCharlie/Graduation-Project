<template>
	<div>
		<el-card class="box-card">
			<div class="text item">
				<i class="el-icon-document-copy" style="margin-right: 10px"></i>
				检验报告审核流程
			</div>
		</el-card>

		<div style="margin: 20px"></div>
		<el-form ref="formItem" label-suffix=":" label-width="150px" label-position="right" size="medium">
			<el-card class="marginTopCard box-card" shadow="never">
				<div slot="header" class="clearfix item">
					<span>
						<i class="el-icon-s-order" style="margin-right: 20px">
							<b>检验报告审核表信息</b>
						</i>
					</span>
				</div>
				<el-row>
					<el-col :span="12">
						<el-form-item class="leftItem" label="申请人">
							<el-input :disabled="true" class="uniflItem" v-model="formItem.createUserName"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item class="rightItem" label="申请部门">
							<el-input :disabled="true" class="uniflItem" v-model="formItem.createDeptName"></el-input>
						</el-form-item>
					</el-col>
				</el-row>

				<el-row>
					<el-col :span="24">
						<!-- <div class="content-view" v-html="rawHtml"></div> -->
						<div style="margin-top: 10px">
							<!-- <vue-ueditor-wrap @ready="ready" v-model="rawHtml" :config="myConfig"></vue-ueditor-wrap> -->
							<ViewProcessReport :reportId="formItem.reportId"></ViewProcessReport>
						</div>
					</el-col>
				</el-row>
			</el-card>

			<el-card class="marginTopCard box-card" shadow="never">
				<div slot="header" class="clearfix item">
					<span>
						<i class="el-icon-s-order" style="margin-right: 20px">
							<b>审核信息</b>
						</i>
					</span>
				</div>
				<el-row>
					<el-col :xl="24" :xs="24">
						<el-form-item class="textareaStyle" label="审核人意见">
							<el-input
								:disabled="reviewDisable.advice_jybgsh"
								v-model="adviceMsg.advice_jybgsh"
								class="intputTextarea"
								type="textarea"
								@dblclick.native="showDefaultOpinions('advice_jybgsh')"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :xl="24" :xs="24">
						<el-form-item class="textareaStyle" label="批准人意见">
							<el-input
								:disabled="reviewDisable.advice_jybgpz"
								v-model="adviceMsg.advice_jybgpz"
								class="intputTextarea"
								type="textarea"
								@dblclick.native="showDefaultOpinions('advice_jybgpz')"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
			</el-card>
			<AdviceWord ref="adviceWord" v-on:setQuickOpinion="setQuickOpinion"></AdviceWord>
		</el-form>
	</div>
</template>

<script>
import '../../../../../public/static/jquery-1.11.3/jquery.min.js';

import { getReportInstace } from '@/api/business/deviceTask';
import { formSave, getFormInfo } from '@/api/business/reportExamine';
import { getFormPrivilege } from '@/api/formPrivilege';
import { getAdviceMsg } from '@/api/process/process';
import AdviceWord from '@/views/common/adviceWord/index';
import Signature from '@/views/system/signature';
import ViewProcessReport from '@/views/core/customTemplate/common/ViewProcessReport';

export default {
	name: 'reportExamine',
	components: {
		AdviceWord,
		Signature,
		ViewProcessReport
	},
	data() {
		return {
			isAuthority: 0, //是否开启权限，start页面不用开启
			reviewDisable: {
				advice_jybgsh: true, //检验报告审核
				advice_jybgpz: true //检验报告批准,
			},
			adviceMsg: {
				advice_jybgsh: '',
				advice_jybgpz: ''
			},
			user: {
				userId: '',
				userName: '',
				nickName: '',
				deptId: '',
				deptName: ''
			},
			rawHtml: '',
			insertSignatureFlag: false,
			formItem: {
				rowId: '',
				reportId: '',
				createUserId: '',
				createUserName: '',
				createDeptId: '',
				createDeptName: ''
			},
			replaceHtmlIsSuccess: false,

			formResult: {},
			goParam: {},
			taskId: '',
			judgeIsUpdate: true,
			formIsSuccess: false,
			currentAdvice: '', //当前流程步骤id，如bmfzrsh,
			formItemOther: {},

			contractId: ''
			// edValue: '',
			// editor: null,
			// myConfig: {
			// 	UEDITOR_HOME_URL: '/static/UEditor/', // 需要令此处的URL等于对应 ueditor.config.js 中的配置。
			// 	// 编辑器不自动被内容撑高
			// 	autoHeightEnabled: false,
			// 	// 初始容器高度
			// 	initialFrameHeight: 450,
			// 	// 初始容器宽度
			// 	initialFrameWidth: '100%',
			// 	//启用自动保存
			// 	enableAutoSave: true,
			// 	//自动保存间隔时间， 单位ms
			// 	saveInterval: 500,
			// 	toolbars: [['print']],
			// 	readonly: true
			// }
		};
	},
	// ------------------------------------------------
	created() {},
	watch: {
		processReturnData(newData, oldName) {
			this.formItemOther = newData;
			this.submitForm();
		},
		formResult(newData, oldName) {
			this.$emit('form-submit-return', newData);
		}
	},
	//---- mounted
	mounted() {
		this.getFormRowId();
		this.getAdviceMsg();
	},
	props: ['formData', 'processReturnData', 'processData'],
	//---- methods
	methods: {
		ready(editorInstance) {
			this.editor = editorInstance;
			this.edValue = this.editor.execCommand('drafts');
			this.editor.setDisabled();
		},
		// 表单提交 ------------- 表单数据方法
		loadReportInstance() {
			getReportInstace(this.formItem.reportId).then(response => {
				let genericContext = response.data.reportGenericContext;
				let templateContext = response.data.reportTemplateContext;

				if (!templateContext && !genericContext) {
					this.rawHtml = '<h2 style="text-align: center;">暂无内容，请先编辑模板内容！</h2>';
				} else {
					this.rawHtml = '<html><body>' + genericContext + templateContext + '</body></html>';
				}
			});
		},

		getFormRowId() {
			var keyVal = this.formData.split('&');
			for (var i = 0; i < keyVal.length; i++) {
				var temp = keyVal[i].split('=');
				this.goParam[temp[0]] = temp[1];
			}
			this.formItem.rowId = this.goParam.rowId;
			this.taskId = this.goParam.taskId;
			this.getFormInfo();
		},
		getFormInfo() {
			getFormInfo(this.formItem.rowId).then(response => {
				if (response.code === 200) {
					this.formItem = response.data;
					this.getFormPrivilege();
				}
			});
		},
		getFormPrivilege() {
			getFormPrivilege({
				taskId: this.taskId
			}).then(response => {
				if (response.code === 200) {
					for (var name in response.data) {
						if (response.data[name] == 'W') {
							this.reviewDisable[name] = false;
							if (name.startsWith('advice_')) {
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
					signControlName: name.split('_')[1]
				}).then(response => {
					if (response.code === 200) {
						for (var i = 0; i < response.data.length; i++) {
							var item = response.data[i];
							this.adviceMsg['advice_' + item.controlName] = item.optionContext;
						}
					}
				});
			}
		},
		submitForm() {
			var subData = {
				entity: this.formItem
			};
			for (var item in this.formItemOther) {
				subData[item] = this.formItemOther[item];
			}
			for (var advice in this.adviceMsg) {
				if (advice === this.currentAdvice) {
					subData[advice] = this.adviceMsg[advice];
				} else {
					subData[advice] = '';
				}
			}
			formSave(subData).then(response => {
				if (response.code === 200) {
					this.formResult = response;
				}
			});
		},
		returnFormValue() {
			this.returnValidValue();
			return new Promise((resolve, reject) => {
				const value = {
					flag: this.formIsSuccess
				};
				console.log(value);
				resolve(value);
			});
		},
		returnValidValue() {
			this.formIsSuccess = true;
		},
		getCreateUserInfo() {
			getCurrentUserInfo().then(response => {
				this.user = response.data;
			});
		},
		resetForm(formName) {
			if (confirm('您确定要重置当前表单吗?')) {
				this.$refs[formName].resetFields();
			} else {
				this.$message.warning('请谨慎操作');
			}
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
		}
	}
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

.content-view {
	margin-left: 60px;
	margin-right: 45px;
	border: 2px #d5d5d5 solid;
	padding: 30px;
	background: #fff;
}
</style>
