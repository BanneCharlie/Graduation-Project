<template>
	<div class="app-container">
		<el-form
			v-show="showSearch"
			:model="queryParams"
			:inline="true"
			size="small"
			ref="queryForm"
			label-suffix=":"
			label-width="100px">
			<el-form-item label="申请人" prop="createUserName">
				<el-input
					v-model="queryParams.createUserName"
					placeholder="请输入申请人"
					clearable
					size="small"
					style="width: 180px"
					@keyup.enter.native="handleQuery" />
			</el-form-item>
			<el-form-item label="合同编号" prop="contractNumber">
				<el-input
					v-model="queryParams.contractNumber"
					placeholder="请输入合同编号"
					clearable
					size="small"
					style="width: 180px"
					@keyup.enter.native="handleQuery" />
			</el-form-item>
			<el-form-item label="申请部门" prop="createDeptName">
				<el-input
					v-model="queryParams.createDeptName"
					placeholder="请输入申请部门"
					clearable
					size="small"
					style="width: 180px"
					@keyup.enter.native="handleQuery"></el-input>
			</el-form-item>
			<el-form-item>
				<el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
					搜索
				</el-button>
				<el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
			</el-form-item>
		</el-form>

		<el-row :gutter="10" class="mb8">
			<!-- <el-col :span="1.5">
          <el-button type="primary" icon="el-icon-check" :disabled="multiple" @click="sendTaskHandle(undefined,'multiple')">批量派发</el-button>
        </el-col> -->
			<right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
		</el-row>

		<el-table
			:data="receiveList"
			style="width: 100%"
			size="mini"
			@selection-change="handleSelectionChange">
			<el-table-column align="center" label="序号" type="index" width="50"></el-table-column>
			<el-table-column
				align="center"
				prop="contractNumber"
				label="合同编号"
				width="180"></el-table-column>
			<el-table-column
				align="center"
				prop="title"
				label="合同申请名称"
				width="250"></el-table-column>
			<el-table-column
				align="center"
				prop="createUserName"
				label="申请人"
				width="100"></el-table-column>
			<el-table-column
				align="center"
				prop="createDeptName"
				label="申请部门"
				width="150"></el-table-column>
			<el-table-column
				align="center"
				prop="deviceCount"
				label="设备数量"
				width="80"></el-table-column>
			<el-table-column
				align="center"
				prop="createTime"
				label="申请时间"
				width="160"></el-table-column>
			<el-table-column align="center" prop="checkUserId" label="是否派发" width="120">
				<template slot-scope="scope">
					<el-tag
						v-if="scope.row.checkUserId == null || scope.row.checkUserId == ''"
						type="danger">
						未派发
					</el-tag>
					<el-tag v-else type="success">已派发</el-tag>
				</template>
			</el-table-column>
			<el-table-column align="center" label="操作" fixed="right">
				<template slot-scope="scope">
					<el-button
						v-if="scope.row.checkUserId == null || scope.row.checkUserId == ''"
						type="success"
						icon="el-icon-success"
						size="mini"
						@click="sendTaskHandle(scope.row)">
						派发任务
					</el-button>
					<el-button
						v-else
						type="warning"
						size="mini"
						icon="el-icon-success"
						@click="sendTaskHandle(scope.row)">
						修改派发信息
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

		<el-dialog
			:title="title"
			:visible.sync="relationDialog"
			width="90%"
			center
			:close-on-click-modal="false"
			:close-on-press-escape="false">
			<el-button
				style="margin-top: 10px; margin-bottom: 30px"
				:disabled="userMlutiple"
				type="danger"
				@click="delSelectUser"
				size="mini">
				批量删除
			</el-button>
			<el-button
				style="margin-top: 10px; margin-bottom: 30px; margin-left: 30px"
				type="success"
				@click="relationUser"
				size="mini">
				添加人员
			</el-button>

			<el-table
				:data="currentPendingUserList"
				size="mini"
				style="width: 100%"
				@selection-change="handleSelectChangeUser">
				<el-table-column align="center" type="selection" width="80"></el-table-column>
				<el-table-column
					align="center"
					label="序号"
					type="index"
					width="80"></el-table-column>
				<el-table-column align="center" label="姓名" prop="nickName"></el-table-column>
				<el-table-column
					align="center"
					label="联系方式"
					prop="phonenumber"></el-table-column>
				<el-table-column
					align="center"
					label="部门名称"
					prop="dept.deptName"></el-table-column>
				<el-table-column align="center" label="操作" width="200">
					<template slot-scope="scope">
						<el-button size="mini" type="danger" @click="delSelectUser(scope.row)">
							删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<pagination
				v-show="userTotal > 0"
				:total="userTotal"
				:page.sync="userQueryParams.pageNum"
				:limit.sync="userQueryParams.pageSize"
				:page-sizes="[5, 10, 15]"
				:page-size="5"
				@pagination="getThisRowUserList(userQueryParams.rowId)"
				style="margin-bottom: 20px" />

			<el-dialog
				title="请选择关联人员"
				:visible.sync="open"
				:show-close="false"
				:close-on-click-modal="false"
				:close-on-press-escape="false"
				width="1000px"
				center
				append-to-body>
				<el-transfer
					v-loading="loadingRelationUserData"
					ref="selectUserTransferRef"
					filterable
					:titles="['人员列表', '本次选择添加人员']"
					:button-texts="['删除', '添加']"
					filter-placeholder="请输入人员名称"
					v-model="hasSelectData"
					:data="selectData"></el-transfer>
				<div slot="footer" class="dialog-footer">
					<el-button
						size="mini"
						type="primary"
						:loading="saveRelationUserLoading"
						@click="saveRelationUser">
						确 定
					</el-button>
					<el-button size="mini" type="danger" @click="cancel">取 消</el-button>
				</div>
			</el-dialog>
		</el-dialog>
	</div>
</template>

<script>
import { selectActiveUser } from '@/api/workflow/workFlowIns/flowInst';
import {
	getAllFinishProcess,
	getSendTaskUserList,
	sendTaskRunning,
	delRelationUser
} from '@/api/business/deviceTask';

export default {
	data() {
		return {
			// 显示搜索条件
			showSearch: true,
			queryParams: {
				pageNum: 1,
				pageSize: 10,
				createUserName: null,
				contractNumber: null,
				createDeptName: null
			},
			userQueryParams: {
				pageNum: 1,
				pageSize: 5,
				rowId: ''
			},
			receiveList: [],
			receiveMap: undefined,

			// 关联人员
			loadingRelationUserData: false,

			title: '',
			relationDialog: false,
			currentPendingUserList: [],
			open: false,
			selectData: [],
			hasSelectData: [],
			lineList: [],

			total: 0,
			userTotal: 0,
			ids: '',
			userIds: '',
			saveRelationUserLoading: false,
			// 非多个禁用
			multiple: true,
			userMlutiple: true
		};
	},
	methods: {
		getList() {
			getAllFinishProcess(this.queryParams).then(response => {
				this.total = response.total;
				this.receiveList = [];
				this.receiveList = this.dataConvert(response.rows);
				this.receiveMap = new Map();
				this.receiveList.forEach(item => {
					this.receiveMap[item.rowId] = item;
				});
				this.saveRelationUserLoading = false;
			});
		},
		/** 搜索按钮操作 */
		handleQuery() {
			this.queryParams.pageNum = 1;
			this.getList();
		},
		/** 重置按钮操作 */
		resetQuery() {
			this.resetForm('queryForm');
			this.handleQuery();
		},
		// 单独获取某个流程表单的 派发人员列表
		getThisRowUserList(rowId) {
			this.userQueryParams.rowId = rowId;
			getSendTaskUserList(this.userQueryParams).then(response => {
				this.currentPendingUserList = response.rows;
				this.userTotal = response.total;
			});
		},

		// 点击派发任务 批量派发 或者单独派发
		sendTaskHandle(row) {
			this.title = '已派工人员列表';
			this.getThisRowUserList(row.rowId);
			this.relationDialog = true;
		},
		// 关联人员 dialog 弹窗
		relationUser() {
			this.loadingRelationUserData = true;
			if (this.selectData == undefined || this.selectData.length == 0) {
				this.generateData();
			}
			setTimeout(() => {
				this.loadingRelationUserData = false;
			}, 1500);
			this.open = true;
			// 单个关联
		},
		// 保存选择的关联人员
		saveRelationUser() {
			if (this.hasSelectData == undefined || this.hasSelectData.length == 0) {
				this.$message.error('您还没有选择人员，不能提交!');
				return;
			}
			this.saveRelationUserLoading = true;
			var userIds = '';
			for (var i = 0; i < this.hasSelectData.length; i++) {
				userIds += this.hasSelectData[i] + ',';
			}
			// 待添加人员id String 串
			userIds = userIds.substring(0, userIds.length - 1);
			let rowIds = [this.userQueryParams.rowId];
			let selectUserIds = userIds.split(',');
			sendTaskRunning(rowIds, selectUserIds).then(response => {
				if (response.code == 200) {
					this.$message.success('添加成功！');
					this.open = false;
					this.relationDialog = false;
					this.getThisRowUserList(this.userQueryParams.rowId);
					this.getList();
				}
			});

			// sendTaskRunning
		},

		// 获取选择人员列表的 数据
		generateData() {
			var appendUserIds = '';
			this.currentPendingUserList.forEach((value, index, array) => {
				// console.log(this.actUserList[index].userId);
				appendUserIds += this.currentPendingUserList[index].userId + ',';
			});
			appendUserIds = appendUserIds.substring(0, appendUserIds.length - 1);
			let existUserIdArr = appendUserIds.split(',');
			selectActiveUser().then(response => {
				this.lineList = response.data;
				for (var i = 0; i < this.lineList.length; i++) {
					let bean = this.lineList[i];
					let beanUserName = bean.userId;
					// console.log(beanUserName + '返回索引值为 -- > ' + (this.checkArryIsExistElement(existUserIdArr,beanUserName)));
					this.selectData.push({
						label: bean.deptName + ' ----- ' + bean.nickName,
						key: parseInt(bean.userId),
						disabled: this.checkArryIsExistElement(existUserIdArr, beanUserName) != -1
					});
				}
				this.open = true;
			});
		},
		delSelectUser(row) {
			let ids = row.userId || this.userIds;
			this.$confirm('您确定要删除选中人员吗?', '警告', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			})
				.then(value => {
					delRelationUser(this.userQueryParams.rowId, ids).then(response => {
						if (response.code == 200) {
							this.$message.success('操作成功!');
						}
						this.getList();
						this.getThisRowUserList(this.userQueryParams.rowId);
					});
				})
				.catch(value => {});
		},
		// 重置选择人员框
		reset() {
			this.selectData = [];
			this.hasSelectData = [];
			this.$refs.selectUserTransferRef.clearQuery('left');
			this.$refs.selectUserTransferRef.clearQuery('right');
		},
		// 取消按钮
		cancel() {
			this.reset();
			this.open = false;
		},

		// 多选框选中数据
		handleSelectionChange(selection) {
			this.ids = selection.map(item => item.rowId);
			this.single = selection.length != 1;
			this.multiple = !selection.length;
		},
		// 多选框选中的人员ids
		handleSelectChangeUser(selection) {
			this.userIds = selection.map(item => item.userId);
			this.userMlutiple = !selection.length;
		},
		//时间格式转换，将时间字符串中带有T的转换为yyyy-MM-dd HH:mm:ss格式的时间字符
		dataConvert(list) {
			var retList = [];
			for (var i = 0; i < list.length; i++) {
				var bean = list[i];
				bean['createTime'] = bean['createTime'].replaceAll('T', ' ').split('.')[0];
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

<style>
.el-transfer-panel__list.is-filterable {
	width: 320px !important;
	height: 380px !important;
}
.el-transfer-panel {
	margin-left: 30px;
	width: 320px !important;
	height: 480px !important;
}
</style>
