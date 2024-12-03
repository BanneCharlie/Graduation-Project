<template>
	<div class="app-container">
		<el-form
			v-show="showSearch"
			ref="queryForm"
			size="mini"
			label-suffix=":"
			:model="queryParams"
			:inline="true">
			<el-form-item label="合同申请名称" prop="title">
				<el-input
					v-model="queryParams.title"
					placeholder="请输入申请名称"
					clearable
					style="width: 180px"
					@keyup.enter.native="handleQuery" />
			</el-form-item>
			<el-form-item label="合同编号" prop="contractNumber">
				<el-input
					v-model="queryParams.contractNumber"
					placeholder="请输入合同编号"
					clearable
					style="width: 180px"
					@keyup.enter.native="handleQuery" />
			</el-form-item>
			<el-form-item label="设备编号" prop="deviceNumber">
				<el-input
					v-model="queryParams.deviceNumber"
					placeholder="请输入设备编号"
					clearable
					style="width: 180px"
					@keyup.enter.native="handleQuery" />
			</el-form-item>
			<el-form-item>
				<el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
					搜索
				</el-button>
				<el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
			</el-form-item>
		</el-form>
		<el-table v-loading="tableLoading" size="mini" style="width: 100%" :data="taskPendingList">
			<el-table-column
				align="center"
				prop="deviceNumber"
				label="设备编号"
				width="240"
				fixed></el-table-column>
			<el-table-column
				align="center"
				prop="deviceName"
				label="设备名称"
				width="240"></el-table-column>
			<el-table-column
				align="center"
				prop="contractNumber"
				label="合同编号"
				width="240"></el-table-column>
			<el-table-column
				align="center"
				prop="title"
				label="合同申请名称"
				width="250"></el-table-column>
			<el-table-column
				align="center"
				prop="updateTime"
				label="领取时间"
				width="150"></el-table-column>
			<el-table-column label="操作" align="center" fixed="right">
				<template slot-scope="scope">
					<el-button
						class="handleBtn"
						type="primary"
						icon="el-icon-edit-outline"
						size="small"
						@click="handleStartCheck(scope.row)">
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
			:page-sizes="[10, 20, 50, 100]"
			:page-size="10"
			@pagination="getList"
			style="margin-bottom: 20px" />
	</div>
</template>

<script>
import { getProcessList, goProcessTemplate } from '@/api/process/process';
import { getFormInfo } from '@/api/business/reqContReview';
import { isEndByPiId } from '@/api/workflow/workFlowIns/flowInst';
import { getTaskPending, getDeviceRelationList } from '@/api/business/deviceTask';

export default {
	name: 'pendingTask',
	data() {
		return {
			tableLoading: false,
			// 日期范围
			dateRange: [],
			// 显示搜索条件
			showSearch: true,
			// 待处理任务查询参数
			queryParams: {
				pageNum: 1,
				pageSize: 5,
				isRead: '1',
				isReceiving: '0',
				title: null,
				contractNumber: null,
				deviceNumber: null
			},

			// 获取当前处理流程人员
			userQueryParams: {
				pageNum: 1,
				pageSize: 5,
				deviceId: null
			},

			total: 0,
			taskPendingList: []
		};
	},
	methods: {
		/** 搜索按钮操作 */
		handleQuery() {
			this.queryParams.pageNum = 1;
			this.getList();
		},
		/** 重置按钮操作 */
		resetQuery() {
			this.dateRange = [];
			this.resetForm('queryForm');
			this.handleQuery();
		},
		//查询所有当前用户的待处理任务（任务管理菜单下）
		getList() {
			getTaskPending(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
				this.total = response.total;
				this.taskPendingList = [];
				this.taskPendingList = this.dataConvert(response.rows);
			});
		},
		//前往检验申请开始页面，处理任务
		handleStartCheck(row) {
			this.userQueryParams.deviceId = row.rowId;
			getDeviceRelationList(this.userQueryParams).then(response => {
				if (response.total != 0) {
					isEndByPiId(row.piid).then(response => {
						if (response.code === 200 && response.data === 1) {
							getFormInfo(row.contractRowId).then(response => {
								if (response.code === 200 && response.data.isRead == 1) {
									this.getProcessInfo(row);
								} else {
									this.$message.error('当前流程已开启检验申请');
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
				this.processInfo = {
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
				fileRelationBeanId: this.processInfo.fileRelationBeanId,
				flag: this.processInfo.flag,
				swId: this.processInfo.swId,
				swtaskId: this.processInfo.swtaskId,
				swtofw: this.processInfo.swtofw,
				type: this.processInfo.type,
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
		//时间格式转换，将时间字符串中带有T的转换为yyyy-MM-dd HH:mm:ss格式的时间字符
		dataConvert(list) {
			var retList = [];
			for (var i = 0; i < list.length; i++) {
				var bean = list[i];
				bean['updateTime'] = bean['updateTime'].replaceAll('T', ' ').split('.')[0];
				retList.push(bean);
			}
			return retList;
		}
	},
	mounted() {
		this.getList();
	}
};
</script>

<style></style>
