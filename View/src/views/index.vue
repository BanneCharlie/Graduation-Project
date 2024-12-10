<template>
	<div class="dashboard-editor-container">
		<panel-group
			@routeLinkByName="selectHeadRouter"
			@getUserFlag="receiveUserDeptFlag" />
		<!-- cardtable -->
		<el-card class="box-card" style="margin-top: -10px">
			<div slot="header" class="clearfix">
				<i class="el-icon-position"></i>
				<span style="margin-left: 10px">事宜速查</span>
			</div>
			<el-tabs>
				<el-tab-pane style="height: auto">
					<span slot="label">
						待办事宜
						<el-badge
							v-if="WaitTotalNumber != 0"
							:value="WaitTotalNumber"
							:max="99"></el-badge>
					</span>
					<el-table
						v-loading="tableLoading"
						size="mini"
						style="width: 100%"
						:data="tableWaitCommitData">
						<el-table-column
							type="index"
							label="序号"
							align="center"
							width="100"></el-table-column>
						<el-table-column
							prop="title"
							label="标题"
							align="center"
							width="380"></el-table-column>
						<el-table-column
							prop="createTime"
							label="时间"
							align="center"
							width="200"></el-table-column>
						<el-table-column
							prop="taskName"
							label="当前环节"
							align="center"
							width="230"></el-table-column>
						<el-table-column
							prop="passUserName"
							label="提交人"
							align="center"
							width="100"></el-table-column>
						<el-table-column
							label="操作"
							align="center"
							width="150">
							<template slot-scope="scope">
								<el-button
									class="handleBtn"
									type="primary"
									icon="el-icon-edit-outline"
									size="mini"
									@click="handClikeRow('update', scope.row)">
									处理
								</el-button>
							</template>
						</el-table-column>
					</el-table>
					<pagination
						v-show="total > 0"
						:total="total"
						:page.sync="queryParams.pageNum"
						:limit.sync="queryParams.pageSize"
						:page-sizes="[5, 10, 15, 20]"
						:page-size="5"
						@pagination="getUserAllProcess"
						style="margin-bottom: 20px" />
				</el-tab-pane>
				<el-tab-pane>
					<span slot="label">
						待处理任务
						<el-badge
							v-if="taskPendingTotal != 0"
							:value="taskPendingTotal"
							:max="99"></el-badge>
					</span>
					<el-table
						v-loading="tableLoading"
						size="mini"
						style="width: 100%"
						:data="taskPendingList">
						<el-table-column
							label="序号"
							type="index"
							width="100"
							align="center"></el-table-column>
						<el-table-column
							align="center"
							prop="deviceNumber"
							label="设备编号"></el-table-column>
						<el-table-column
							align="center"
							prop="deviceName"
							label="设备名称"></el-table-column>
						<el-table-column
							label="操作"
							align="center"
							width="300">
							<template slot-scope="scope">
								<el-button
									class="handleBtn"
									type="primary"
									icon="el-icon-edit-outline"
									size="mini"
									@click="handleStartCheck(scope.row)">
									处理
								</el-button>
							</template>
						</el-table-column>
					</el-table>
					<pagination
						v-show="total3 > 0"
						:total="total3"
						:page.sync="queryParams3.pageNum"
						:limit.sync="queryParams3.pageSize"
						:page-sizes="[5, 10, 15, 20]"
						:page-size="5"
						@pagination="getTaskPending"
						style="margin-bottom: 20px" />
				</el-tab-pane>
				<el-tab-pane v-if="userDeptIsFinance">
					<span slot="label">
						待确认打印
						<el-badge
							v-if="waitConfirmPrintTotal != 0"
							:value="waitConfirmPrintTotal"
							:max="99"></el-badge>
					</span>
					<el-table
						v-loading="tableLoading"
						size="mini"
						style="width: 100%"
						:data="waitConfirmPrintData">
						<el-table-column
							label="序号"
							type="index"
							width="60"></el-table-column>
						<el-table-column
							prop="title"
							label="标题"
							align="center"></el-table-column>
						<el-table-column
							prop="createTime"
							label="创建时间"
							align="center"
							width="170"></el-table-column>
						<el-table-column
							prop="createUserName"
							label="提交人"
							align="center"
							width="230"></el-table-column>
						<el-table-column
							label="操作"
							align="center"
							width="300">
							<template slot-scope="scope">
								<el-button
									class="handleBtn"
									type="primary"
									icon="el-icon-view"
									size="mini"
									@click="handleViewProcess(scope.row)">
									查看
								</el-button>
								<el-button
									class="handleBtn"
									type="success"
									icon="el-icon-view"
									size="mini"
									@click="
										handleClickConfirmFinancePrint(
											scope.row
										)
									">
									确认通过
								</el-button>
							</template>
						</el-table-column>
					</el-table>
					<pagination
						v-show="waitConfirmPrintTotal > 0"
						:total="waitConfirmPrintTotal"
						:page.sync="waitConfirmQueryParam.pageNum"
						:limit.sync="waitConfirmQueryParam.pageSize"
						:page-sizes="[5, 10, 15, 20]"
						:page-size="5"
						@pagination="getFinanceData"
						style="margin-bottom: 20px" />
				</el-tab-pane>
			</el-tabs>
		</el-card>
		<!-- echarts  TODO 数据代待优化处理-->
		<el-card class="box-card">
			<div slot="header" class="clearfix">
				<i class="el-icon-data-line"></i>
				<span style="margin-left: 10px">综合统计</span>
			</div>
			<el-row :gutter="32">
				<el-col :xs="24" :sm="24" :lg="14">
					<div class="ColHead">
						<i class="el-icon-pie-chart">
							<span style="font-size: 18px; margin-left: 12px">
								业务办理统计
							</span>
						</i>
					</div>
					<div class="chart-wrapper">
						<bar-chart />
					</div>
				</el-col>

				<el-col :xs="24" :sm="24" :lg="10">
					<div class="ColHead" style="margin-left: 80px">
						<i class="el-icon-pie-chart">
							<span style="font-size: 18px; margin-left: 12px">
								检验设备统计
							</span>
						</i>
					</div>
					<div class="chart-wrapper">
						<pie-chart />
					</div>
				</el-col>
			</el-row>
		</el-card>
	</div>
</template>

<script>
import PanelGroup from '@/views/dashboard/PanelGroup';
import PieChart from '@/views/dashboard/PieChart';
import BarChart from '@/views/dashboard/BarChart';
import NotHandleTable from '@/views/dashboard/table/NotHandleTable';
import {
	getEndLoginInfo,
	setRedisCacheNotice
} from '@/api/business/reportExamine';

import {
	getUserAllProcess,
	getUserAllHistoryProcess,
	handleRecall,
	goRunProcessTemplate,
	goProcessTemplate,
	getProcessList
} from '@/api/process/process';
import {
	getTaskPending,
	getTasksProcessed,
	getDeviceRelationList
} from '@/api/business/deviceTask';
import { isEndByPiId } from '@/api/workflow/workFlowIns/flowInst';
import { getFormInfo } from '@/api/business/reqContReview';

import {
	getFinanceConfirmList,
	confirmPrintByContractRowId
} from '@/api/business/finance/FinanceConfirm';

export default {
	name: 'Index',
	data() {
		return {
			tableLoading: false,
			tableWaitCommitData: [],
			tableAlreadyHandleData: [],
			// 待办事宜查询参数
			queryParams: {
				pageNum: 1,
				pageSize: 5,
				actName: undefined,
				procDefId: undefined
			},
			//已办事宜查询参数
			queryParams2: {
				pageNum: 1,
				pageSize: 5,
				actName: undefined,
				procDefId: undefined
			},
			//待处理任务查询参数
			queryParams3: {
				pageNum: 1,
				pageSize: 5,
				isRead: '1',
				isReceiving: '0'
			},
			//已处理任务查询参数
			queryParams4: {
				pageNum: 1,
				pageSize: 5,
				isRead: '0',
				isReceiving: '0'
			},
			// 获取当前处理流程人员
			userQueryParams: {
				pageNum: 1,
				pageSize: 5,
				deviceId: null
			},
			// 日期范围
			dateRange: [],
			// 待办事宜总条数
			total: 0,
			// 已办事宜总条数
			total2: 0,
			//待处理任务总条数
			total3: 0,
			//已处理任务总条数
			total4: 0,
			processInfo: {
				processKey: '',
				businessKey: '',
				processInstId: '',
				taskId: '',
				templateType: '',
				blfs: '1',
				toDoType: 'all'
			},
			processPendingInfo: {},
			sxpzInfo: {},
			//待办总条数
			WaitTotalNumber: 0,
			taskPendingList: [],
			taskPendingTotal: 0,
			taskProcessedList: [],

			userDeptIsFinance: false,
			// 财务人员 - 待确认打印记录
			waitConfirmPrintData: [],
			waitConfirmPrintTotal: 0,
			waitConfirmQueryParam: {
				pageNum: 1,
				pageSize: 5
			}
		};
	},
	components: {
		PanelGroup,
		PieChart,
		BarChart,
		NotHandleTable
	},
	created() {
		this.getNewInsertReportInfo();
	},
	methods: {
		getNewInsertReportInfo() {
			getEndLoginInfo().then(response => {
				let resultVo = response.data;
				if (resultVo != null) {
					if (resultVo.lastLoginTime != null) {
						this.$notify({
							title: '上一次登录时间!',
							dangerouslyUseHTMLString: true,
							message:
								'您上次登录时间为:<p style="text-align: center;font-size: 20px; color:red;">' +
								resultVo.lastLoginTime +
								'</p>',
							type: 'success'
						});
					}
					setTimeout(() => {
						if (resultVo.reportNumbers != null) {
							let deviceNumber = '';
							resultVo.reportNumbers.forEach(
								(value, index, array) => {
									deviceNumber += value + '</br>';
								}
							);
							this.$notify({
								title: '新的报告审核！',
								dangerouslyUseHTMLString: true,
								message:
									'距您上次登录之后共有:<span style="color:red;font-size: 25px;"><i>' +
									resultVo.reportNumbers.length +
									'条</span></i>&nbsp;&nbsp;&nbsp;新的报告已经通过审核!' +
									'</br>报告编号为:</br><span style="color:red;font-size: 18px;">' +
									deviceNumber +
									'</span>',
								duration: 5500,
								type: 'success',
								showClose: false
							});
						}
					}, 1000);
					setRedisCacheNotice();
				}
			});
		},

		selectHeadRouter(type) {
			// console.log(type);
			switch (type) {
				// 委托合同申请
				case 'requestContract':
					this.$router.push({
						path: 'checkwork/contractReviewList'
					});
					break;
				// 检验模板
				case 'requestReport':
					this.$router.push({
						path: 'checkwork/reportManageList'
					});
					break;
				case 'waitSendTask':
					this.$router.push({
						path: 'taskManagement/sendWorks'
					});
					break;
				// 待领取任务
				case 'taskReceive':
					this.$router.push({
						path: 'taskManagement/taskReceive'
					});
					break;
				// 综合查询
				case 'integratedQuery':
					this.$router.push({
						path: 'comprequery/checkCotractQuery'
					});
					break;
				// 产品中心检验
				case 'productCheckCenter':
					this.$router.push({
						path: 'pruductcenter/proCotractReview'
					});
					break;
        // 我的报告
        case 'myReport':
          this.$router.push({
            path: 'taskManagement/myReport'
          });
          break;
			}
		},
		//获取当前用户所有待办事项
		getUserAllProcess() {
			getUserAllProcess(
				this.addDateRange(this.queryParams, this.dateRange)
			).then(response => {
				this.total = response.total;
				this.tableWaitCommitData = [];
				this.tableWaitCommitData = this.dataConvert(
					response.rows,
					'createTime'
				);
				this.WaitTotalNumber = response.total;
			});
		},
		//获取当前用户所有已办事项
		getUserAllHistoryProcess() {
			getUserAllHistoryProcess(
				this.addDateRange(this.queryParams2, this.dateRange)
			).then(response => {
				this.total2 = response.total;
				this.tableAlreadyHandleData = [];
				this.tableAlreadyHandleData = this.dataConvert(
					response.rows,
					'createTime'
				);
			});
		},
		//查询所有当前用户的待处理任务（任务管理菜单下）
		getTaskPending() {
			getTaskPending(
				this.addDateRange(this.queryParams3, this.dateRange)
			).then(response => {
				this.total3 = response.total;
				this.taskPendingList = [];
				this.taskPendingList = this.dataConvert(
					response.rows,
					'updateTime'
				);
				this.taskPendingTotal = response.total;
			});
		},
		//查询当前用户所有已处理任务（任务管理菜单下）
		getTaskProcessed() {
			getTasksProcessed(
				this.addDateRange(this.queryParams4, this.dateRange)
			).then(response => {
				this.total4 = response.total;
				this.taskProcessedList = [];
				this.taskProcessedList = this.dataConvert(
					response.rows,
					'createTime'
				);
			});
		},
		//进入更新页面  templateType=update为更新，=view为查看
		handClikeRow(templateType, row) {
			goRunProcessTemplate({
				processKey: row.ptId,
				businessKey: row.businessKey,
				processInstId: row.piId,
				taskId: row.taskId,
				templateType: templateType,
				blfs: 1,
				toDoType: 'all'
			}).then(response => {
				var tpType =
					templateType == 'update'
						? 'templatePageUpdate'
						: 'templatePageView';
				this.$router.push({
					name: tpType,
					params: { processUrlVoJson: JSON.stringify(response.data) }
				});
			});
		},
		handleViewProcess(row) {
			goRunProcessTemplate({
				processKey: row.ptid,
				businessKey: row.businessKey,
				processInstId: row.piid,
				templateType: 'view'
			}).then(response => {
				var tpType = 'templatePageView';
				this.$router.push({
					name: tpType,
					params: { processUrlVoJson: JSON.stringify(response.data) }
				});
			});
		},
		//撤回按钮
		handleRecall(row) {
			this.$confirm(
				'是否确定撤回该已办事项：' + row.title + ', 是否继续?',
				'提示',
				{
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}
			).then(() => {
				handleRecall(row.mainRowId).then(response => {
					this.$message({
						showClose: true,
						message: response.msg,
						type: response.code === 200 ? 'success' : 'error'
					});
					this.getUserAllProcess();
					this.getUserAllHistoryProcess();
					this.getTaskPending();
					this.getTaskProcessed();
				});
			});
		},

		/********* 待处理任务 *********/
		//点击处理（由当前委托合同申请开启检验申请）
		//前往检验申请开始页面，处理任务
		//前往检验申请开始页面，处理任务
		handleStartCheck(row) {
			this.userQueryParams.deviceId = row.rowId;
			getDeviceRelationList(this.userQueryParams).then(response => {
				if (response.total != 0) {
					isEndByPiId(row.piid).then(response => {
						if (response.code === 200 && response.data === 1) {
							getFormInfo(row.contractRowId).then(response => {
								if (
									response.code === 200 &&
									response.data.isRead == 1
								) {
									this.getProcessInfo(row);
								} else {
									this.$message.error(
										'当前流程已开启检验申请'
									);
								}
							});
						}
					});
				} else {
					this.$message.error('您未选择关联人员！');
				}
			});
		},
		getProcessInfo(row) {
			getProcessList('key-report-manage').then(response => {
				this.sxpzInfo = response.data.sxpzList[0];
				this.processPendingInfo = {
					fileRelationBeanId: response.data.fileRelationBeanId,
					flag: response.data.flag,
					swId: response.data.swId,
					swtaskId: response.data.swtaskId,
					swtofw: response.data.swtofw,
					type: response.data.type
				};
				this.goProcessTemplate(row);
			});
		},
		goProcessTemplate(row) {
			goProcessTemplate({
				fileRelationBeanId: this.processPendingInfo.fileRelationBeanId,
				flag: this.processPendingInfo.flag,
				swId: this.processPendingInfo.swId,
				swtaskId: this.processPendingInfo.swtaskId,
				swtofw: this.processPendingInfo.swtofw,
				type: this.processPendingInfo.type,
				processKey: this.sxpzInfo.ptId,
				businessKey: this.sxpzInfo.sxId,
				sxpzId: this.sxpzInfo.sxpzRowId,
				dwOrgId: this.sxpzInfo.deptId,
				templateType: 'start',
				processInstId: '',
				contractId: row.contractRowId,
				deviceRowId: row.rowId
			}).then(response => {
				this.$router.push({
					name: 'templatePage',
					params: { processUrlVoJson: JSON.stringify(response.data) }
				});
			});
		},

		//已处理任务
		handleDataView(row) {
			goProcessTemplate({
				fileRelationBeanId: '',
				flag: '',
				swId: '',
				swtaskId: '',
				swtofw: '',
				type: '',
				processKey: '',
				businessKey: 'key-report-manage',
				sxpzId: '',
				dwOrgId: '',
				templateType: 'view',
				processInstId: row.piid
			}).then(response => {
				this.$router.push({
					name: 'templatePageView',
					params: { processUrlVoJson: JSON.stringify(response.data) }
				});
			});
		},

		// =============================================================
		// ========= 2022-8-17 新增财务人员确认打印年终报告记录 ==========
		receiveUserDeptFlag() {
			// 财务人员获取
			this.getFinanceData();
			this.userDeptIsFinance = true;
		},
		// 获取财务人员需要确认的审批
		getFinanceData() {
			getFinanceConfirmList(this.waitConfirmQueryParam).then(response => {
				this.waitConfirmPrintData = response.rows;
				this.waitConfirmPrintTotal = response.total;
			});
		},
		// 点击确认处理通过
		handleClickConfirmFinancePrint(row) {
			this.$confirm('您确定要执行确认通过操作吗?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning',
				center: true
			})
				.then(() => {
					confirmPrintByContractRowId(row.rowId).then(response => {
						this.$message.success('操作成功!');
						this.getFinanceData();
					});
				})
				.catch(() => {});
		},

		//时间格式转换，将时间字符串中带有T的转换为yyyy-MM-dd HH:mm:ss格式的时间字符
		dataConvert(list, filedName) {
			var retList = [];
			for (var i = 0; i < list.length; i++) {
				var bean = list[i];
				bean[filedName] = bean[filedName]
					.replaceAll('T', ' ')
					.split('.')[0];

				retList.push(bean);
			}
			return retList;
		}
	},
	mounted: function () {
		this.getUserAllProcess();
		this.getUserAllHistoryProcess();
		this.getTaskPending();
		// this.getTaskProcessed()
	}
};
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
	padding: 32px;
	background-color: rgb(240, 242, 245);
	position: relative;

	.chart-wrapper {
		background: #fff;
		padding: 16px 16px 0;
		margin-bottom: 32px;
	}
}

@media (max-width: 1024px) {
	.chart-wrapper {
		padding: 8px;
	}
}
.ColHead {
	margin-left: 70px;
	padding-bottom: -20px;
}
.text {
	font-size: 14px;
}

.item {
	margin-bottom: 18px;
}

.clearfix:before,
.clearfix:after {
	display: table;
	content: '';
}
.clearfix:after {
	clear: both;
}

.box-card {
	width: 100%;
	margin-top: 15px;
}
</style>
