<template>
	<div ckass="tops">
		<div class="dashboard-editor-container">
			<FromComponent
				ref="fromComponent"
				:formData="formData"
				:processData="processData"
				:processReturnData="processRetData"
				:attachmentIds="attachmentIds"
				@handle-change-number="changeNumberData"
				@form-submit-return="formSubmitReturn"
				@previous-step-ids="handlePreviousStepIds"></FromComponent>
		</div>

		<!-- 附件组件 -->
		<div class="dashboard-editor-container" v-if="isAttachment == true && !formComponentIsReport">
			<AttachmentComponent
				ref="attachmentComponent"
				:attachmentParam="attachmentItem"
				:previousIds="previousStepIds"
				@child-return-ids="handleChildAttachmentReturnIds"></AttachmentComponent>
		</div>
		<!-- 设备组件 -->
		<div class="dashboard-editor-container-advice" v-if="isAdvice == true">
			<AdviceComponent :piid="piid"></AdviceComponent>
		</div>
		<!-- 流程组件 -->
		<div class="dashboard-editor-container" v-if="isProcess == true && !formComponentIsReport">
			<ProcessComponent
				ref="processComponent"
				:processData="processData"
				@show-dialog-and-device-template="showDialogAndDeviceTemplate"
				@is-end-node="isEndNode"></ProcessComponent>
		</div>
		<!-- 操作组件 -->
		<div class="dashboard-editor-container" v-if="isOperate == true && !formComponentIsReport">
			<OperateComponent @operate-click="operateClick" @operate-return="operateReturn"></OperateComponent>
		</div>
		<el-backtop>
			<div
				style="
					 {
						height: 100%;
						width: 100%;
						background-color: #f2f5f6;
						box-shadow: 0 0 6px rgba(0, 0, 0, 0.12);
						text-align: center;
						line-height: 40px;
						color: #1989fa;
					}
				">
				Top
			</div>
		</el-backtop>
	</div>
</template>

<script>
import Vue from 'vue';
import { handleCurrentNumber } from '@/api/business/businessNumber';

export default {
	name: 'templatePage',
	data() {
		return {
			loading: [],
			processUrlVo: {},
			formData: '',
			processData: '',
			processRetData: undefined,

			currentProcessBusinessKey: '',
			formComponentIsReport: false,
			formResult: undefined,
			isAdvice: false,
			isProcess: false,
			isOperate: false,
			isAttachment: false,
			piid: '',
			formResult: undefined,
			formIsSuccess: false,

			// 表单重复提交
			isCommit: false,

			// 销毁合同编号字段
			isNormalClosePage: false,
			currentDeviceCategoty: '',
			currentNumber: '',

			// 附件相关
			attachmentItem: {
				processType: '', // 当前流程类型 start , update , view
				createUserNickName: '', // 当前登录用户的真实用户名
				category: '' // 文件类别
			},

			// component prop -- emit()件传值
			attachmentIds: '', // 本次上传附件ids

			previousStepIds: ''
		};
	},
	watch: {
		// previousStepIds(newData,oldData ){
		//   console.log('template newData previousStepIds' + newData);
		// }
	},
	mounted: function () {
		document.onkeydown = function (e) {
			var ev = window.event || e;
			var code = ev.keyCode || ev.which;
			if (code == 116) {
				if (ev.preventDefault) {
					ev.preventDefault();
				} else {
					ev.keyCode = 0;
					ev.returnValue = false;
				}
			}
		};
	},
	// 实例销毁前执行的方法
	beforeDestroy() {
		if (this.attachmentItem.processType == 'start' && this.currentProcessBusinessKey == 'key-contract-review') {
			if (this.isNormalClosePage) {
				console.log('正常关闭当前标签页');
			} else {
				// console.log('非正常关闭');
				if (this.currentNumber != '' && this.currentDeviceCategoty != '') {
					handleCurrentNumber(this.currentDeviceCategoty, this.currentNumber).then(response => {
						if (response.code == 200) {
							console.log('close current page cache clear is ok!' + this.currentDeviceCategoty);
						}
					});
				}
			}
		}
	},
	methods: {
		// 每次提交流程都在该流程上添加笼罩层
		executeLoadingPage(message) {
			let setMessage = !message ? '请稍候......' : '提交数据中...';
			this.loading = this.$loading({
				lock: true,
				text: setMessage,
				spinner: 'el-icon-loading',
				background: 'rgba(0, 0, 0, 0.7)'
			});
		},
		operateClick() {
			if (this.isCommit) {
				this.$message.error('您已经提交过了，请等待服务器处理数据!');
				return;
			}
			this.$refs.fromComponent.returnFormValue().then(value => {
				this.formIsSuccess = value.flag;
				if (!this.formIsSuccess) {
					return;
				}
				this.executeLoadingPage();
				if (this.isAttachment) {
					this.$refs.attachmentComponent
						.checkHasWaitAttachment()
						.then(value => {
							// console.log(value);
							this.formIsSuccess = value.flag;
							if (this.formIsSuccess) {
								this.isNormalClosePage = true;
								this.$refs.processComponent
									.submitProcessData()
									.then(val => {
										if (val != null) {
											this.isCommit = true;
											this.processRetData = val;
										}
										this.loading.close();
									})
									.catch(value => {
										this.loading.close();
									});
							}
						})
						.catch(value => {
							this.loading.close();
						});
				} else {
					if (this.formIsSuccess) {
						this.isNormalClosePage = true;
						this.$refs.processComponent
							.submitProcessData()
							.then(val => {
								if (val != null) {
									this.isCommit = true;
									this.processRetData = val;
								}
								this.loading.close();
							})
							.catch(() => {
								this.loading.close();
							});
					}
				}
			});
		},
		operateReturn() {
			this.closeCurrentPage();
		},
		formSubmitReturn(res) {
			var flag = 'success';
			if (this.attachmentItem.processType == 'start' && this.currentProcessBusinessKey == 'key-contract-review') {
				// 再存入数据库 之后就将该 缓存清除
				// console.log(this.isNormalClosePage);
				handleCurrentNumber(this.currentDeviceCategoty, this.currentNumber).then(response => {
					if (response.code == 200) {
						console.log('success commit clear current cache is success!' + this.currentDeviceCategoty);
					}
				});
			}
			if (res.code === 200) {
				this.closeCurrentPage();
			} else {
				flag = 'error';
			}
			this.openMessage(res.msg, flag);
		},
		openMessage(msg, flag) {
			this.$message({
				showClose: true,
				message: msg,
				type: flag
			});
		},
		changeNumberData(changeData) {
			this.currentDeviceCategoty = changeData.handleDeviceCagegory;
			this.currentNumber = changeData.handleNumber;
		},
		//关闭当前标签页
		closeCurrentPage() {
			this.$store.state.tagsView.visitedViews.splice(
				this.$store.state.tagsView.visitedViews.findIndex(item => item.path === this.$route.path),
				1
			);
			this.$router.push(this.$store.state.tagsView.visitedViews[0].path);
		},
		// 附件回传ids到表单组件
		handleChildAttachmentReturnIds(attachmentIds) {
			this.attachmentIds = attachmentIds.substring(0, attachmentIds.length - 1);
			// console.log('传回到template 本次文件的 ids -->' + this.attachmentIds);
		},
		// 从子组件 业务表单中获取 之前流程的文件id
		handlePreviousStepIds(previousStepIds) {
			this.$nextTick(() => {
				this.previousStepIds = previousStepIds;
				// console.log('template-->previousStepIds-->' + this.previousStepIds);
			});
		},
		showDialogAndDeviceTemplate(flag) {
			this.$refs.fromComponent.showPeopleAndDialog(flag);
		},
		//判断当前节点是否为最后一步
		isEndNode() {
			this.$refs.fromComponent.isProcessEndNode();
		}
	},
	created() {
		this.processUrlVo = JSON.parse(this.$route.params.processUrlVoJson);
		// console.log(this.processUrlVo)
		//表单组件
		this.currentProcessBusinessKey = this.processUrlVo.businessKey + '';
		// console.log(this.processUrlVo.businessKey)
		// console.log(this.currentProcessBusinessKey);
		// console.log(this.formComponentIsReport);
		if (this.currentProcessBusinessKey == 'key-report-manage') {
			this.formComponentIsReport = true;
		}
		// console.log(this.formComponentIsReport);
		var formItem = this.processUrlVo.formUrl.split('?');
		this.formData = formItem[1];
		Vue.component('FromComponent', require('@/views' + formItem[0] + '/index.vue').default);

		//流程组件
		var processurl = this.processUrlVo.processUrl;
		if (processurl != null && processurl != '') {
			this.isProcess = true;
			var processItem = processurl.split('?');
			Vue.component('ProcessComponent', require('@/views' + processItem[0] + '/index.vue').default);
			this.processData = processItem[1];
		}

		//按钮组件
		var operatUrl = this.processUrlVo.operateUrl;
		if (operatUrl != null && operatUrl != '') {
			this.isOperate = true;
			Vue.component('OperateComponent', require('@/views' + operatUrl + '/index.vue').default);
		}

		//流程过程组件
		var adviceUrl = this.processUrlVo.adviceUrl;
		if (adviceUrl != null && adviceUrl != '') {
			this.isAdvice = true;
			var adviceItem = adviceUrl.split('?');
			Vue.component('AdviceComponent', require('@/views' + adviceItem[0] + '/index.vue').default);
			this.piid = adviceItem[1].split('=')[1];
		}

		// 流程附件组件
		var attachmentUrl = this.processUrlVo.fileUrl;
		if (attachmentUrl != null && attachmentUrl != '') {
			this.isAttachment = true;
			var attachmentItem = attachmentUrl.split('?');
			Vue.component('AttachmentComponent', require('@/views' + attachmentItem[0] + '/index.vue').default);
			this.attachmentItem.processType = attachmentItem[1].split('&')[0].split('=')[1];
			this.attachmentItem.createUserNickName = attachmentItem[1].split('&')[1].split('=')[1];
			this.attachmentItem.category = attachmentItem[1].split('&')[2].split('=')[1];
		}
	}
};
</script>

<style lang="scss">
.dashboard-editor-container {
	padding-left: 15px;
	padding-right: 15px;
	padding-top: 15px;
	background-color: rgb(240, 242, 245);
	position: relative;
}
.dashboard-editor-container-advice {
	padding-left: 10px;
	padding-right: 10px;
	padding-top: 15px;
	background-color: rgb(240, 242, 245);
	position: relative;
}
.item {
	padding: 5px !important;
}
.el-input.is-disabled .el-input__inner {
	background-color: #f9f9f9 !important;
}
.el-input.is-disabled .el-input__inner {
	background-color: #f5f7fa;
	border-color: #dfe4ed;
	color: #303133;
	cursor: not-allowed;
}
.el-textarea.is-disabled .el-textarea__inner {
	background-color: #f9f9f9 !important;
	border-color: #dfe4ed;
	color: #303133;
	cursor: not-allowed;
}
.el-transfer-panel__list.is-filterable {
	width: 320px !important;
	height: 380px !important;
}
.el-transfer-panel {
	margin-left: 30px;
	width: 320px !important;
	height: 480px !important;
}
/*  */
</style>
