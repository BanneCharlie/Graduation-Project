<template>
	<div>
		<el-card class="box-card">
			<div class="text item">
				<i class="el-icon-document-copy" style="margin-right: 10px"></i>
				检验报告审核流程
			</div>
		</el-card>

		<div style="margin: 20px"></div>
		<el-form ref="formItem" label-suffix=":" label-width="150px" label-position="right" size="mini">
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
							<el-input :disabled="true" class="uniflItem" v-model="user.nickName"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item class="rightItem" label="申请部门">
							<el-input :disabled="true" class="uniflItem" v-model="user.deptName"></el-input>
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
		</el-form>
	</div>
</template>

<script>
import { getCurrentUserInfo } from '@/api/system/user';
import { getReportInstace } from '@/api/business/deviceTask';
import { formSave } from '@/api/business/reportExamine';

import ViewProcessReport from '@/views/core/customTemplate/common/ViewProcessReport';

export default {
	name: 'reportExamine',
	components: { ViewProcessReport },
	data() {
		return {
			isAuthority: 0, //是否开启权限，start页面不用开启
			reviewDisable: {
				advice_jybgsh: true, //检验报告审核
				advice_jybgpz: true //检验报告批准,
			},
			user: {
				userId: '',
				userName: '',
				nickName: '',
				deptId: '',
				deptName: ''
			},
			rawHtml: '',
			formIsSuccess: false,

			formItem: {
				reportId: null,
				reportCategory: null
			},
			formIsSuccess: false,
			formItemOther: {},

			contractId: '',
			edValue: '',
			editor: null,
			myConfig: {
				UEDITOR_HOME_URL: '/static/UEditor/', // 需要令此处的URL等于对应 ueditor.config.js 中的配置。
				// 编辑器不自动被内容撑高
				autoHeightEnabled: false,
				// 初始容器高度
				initialFrameHeight: 450,
				// 初始容器宽度
				initialFrameWidth: '100%',
				//启用自动保存
				enableAutoSave: true,
				//自动保存间隔时间， 单位ms
				saveInterval: 500,
				toolbars: [['print']],
				readonly: true
			}
		};
	},
	watch: {
		processReturnData(newData, oldData) {
			this.formItemOther = newData;
			this.submitForm();
		}
	},
	// ------------------------------------------------
	created() {},
	//---- mounted
	mounted() {
    this.initalPageParams();
		this.getCreateUserInfo();
	},
	props: ['formData', 'processReturnData', 'processData'],
	//---- methods
	methods: {

    // 初始化页面参数信息
		initalPageParams() {
			let baseUrl = this.processData.split('&');
			let childrenUrl = baseUrl[baseUrl.length - 1].split('=');
			this.formItem.reportId = childrenUrl[childrenUrl.length - 1];
		},

		submitForm() {
			var subData = {
				entity: this.formItem
			};
			for (var item in this.processReturnData) {
				subData[item] = this.processReturnData[item];
			}
			formSave(subData).then(response => {
				if (response.code === 200) {
					this.$emit('form-submit-return', response);
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
