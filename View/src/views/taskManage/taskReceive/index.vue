<template>
	<div class="app-container">
		<el-form
			:model="queryParams"
			ref="queryForm"
			v-show="showSearch"
			:inline="true"
			label-suffix=":"
			size="mini">
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
			<el-form-item label="领取状态" prop="isReceiving">
				<el-select
					v-model="queryParams.isReceiving"
					placeholder="领取状态"
					clearable
					style="width: 130px">
					<el-option
						v-for="status in receivingStatus"
						:key="status.statusValue"
						:label="status.statusLabel"
						:value="status.statusValue" />
				</el-select>
			</el-form-item>
			<el-form-item>
				<el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
					搜索
				</el-button>
				<el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
			</el-form-item>
		</el-form>

		<el-row :gutter="10" class="mb8">
			<el-col :span="1.5">
				<el-button
					size="mini"
					type="primary"
					icon="el-icon-finished"
					:disabled="multiple"
					@click="handReviceAll">
					一键领取
				</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button
					size="mini"
					type="danger"
					icon="el-icon-refresh-left"
					:disabled="multiple"
					@click="handleRevert">
					一键归还
				</el-button>
			</el-col>
			<right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
		</el-row>

		<el-table
			row-key="rowId"
			style="width: 100%"
			size="small"
			:tree-props="{ children: 'children' }"
			:data="receiveMapList"
			@selection-change="handleSelectionChange">
			<el-table-column
				align="center"
				type="selection"
				:selectable="selectChangeStatus"
				width="50"
				fixed="left" />
			<el-table-column
				align="center"
				prop="contractNumber"
				label="合同编号"
				width="200"></el-table-column>
			<el-table-column
				align="center"
				prop="createTime"
				label="创建时间"
				width="170"></el-table-column>
			<el-table-column
				align="center"
				prop="deviceNumber"
				label="报告编号"
				width="210"></el-table-column>
			<el-table-column
				align="center"
				prop="deviceName"
				label="设备名称"
				width="100"></el-table-column>
			<el-table-column
				align="center"
				prop="title"
				label="合同申请名称"
				width="230"></el-table-column>
			<!-- <el-table-column align="center" label="状态" width="80">
        <template slot-scope="scope">
          <span v-if="!scope.row.isMenu">
            <el-tag
              v-if="
                scope.row.isRead == null ||
                scope.row.isRead == '' ||
                scope.row.isRead == '1'
              "
              type="danger"
              >未完成</el-tag
            >
            <el-tag v-else type="success">已完成</el-tag>
          </span>
        </template>
      </el-table-column> -->
			<el-table-column
				align="center"
				prop="receivingUserName"
				label="领取人"
				width="100"></el-table-column>
			<el-table-column
				align="center"
				prop="updateTime"
				label="领取时间"
				width="150"></el-table-column>
			<el-table-column align="center" label="操作" width="350" fixed="right">
				<template slot-scope="scope">
					<span v-if="!scope.row.isMenu">
						<span v-if="scope.row.isRead != '0'">
							<el-button
								:disabled="scope.row.isReceiving == '0'"
								size="mini"
								class="handleBtn"
								type="success"
								icon="el-icon-success"
								@click="handTask(1, scope.row)">
								领取
							</el-button>
							<el-button
								:disabled="
									!(
										scope.row.isReceiving == '0' &&
										scope.row.receivingUserId == user.userId
									) || scope.row.isRead == '0'
								"
								size="mini"
								class="handleBtn"
								type="danger"
								icon="el-icon-circle-close"
								@click="handTask(2, scope.row)">
								归还
							</el-button>
							<!-- <el-button
                :disabled="
                  !(
                    scope.row.isReceiving == '0' &&
                    scope.row.receivingUserId == user.userId
                  ) || scope.row.isRead == '0'
                "
                class="handleBtn"
                type="primary"
                icon="el-icon-thumb"
                
                @click="handleStartCheck(scope.row)"
                >处理</el-button
              > -->
						</span>
						<el-button
							v-if="
								scope.row.isReceiving == '0' &&
								scope.row.receivingUserId == user.userId
							"
							size="mini"
							class="handleBtn"
							type="warning"
							icon="el-icon-connection"
							@click="clickRelationUsers(scope.row)">
							关联人员
						</el-button>
					</span>
				</template>
			</el-table-column>
		</el-table>
		<!-- <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="10"
      @pagination="getList"
      style="margin-bottom: 20px"
    /> -->

		<el-dialog
			title="关联人员列表"
			width="60%"
			center
			@close="clearDeviceId"
			:visible.sync="relationDialog"
			:close-on-click-modal="false"
			:close-on-press-escape="false">
			<el-button
				size="mini"
				style="margin-top: 10px; margin-bottom: 30px"
				:disabled="multipleUser"
				type="danger"
				@click="delSelectUser">
				批量删除
			</el-button>
			<el-button
				size="mini"
				style="margin-top: 10px; margin-bottom: 30px; margin-left: 30px"
				type="success"
				@click="clickViewUserLists">
				添加人员
			</el-button>

			<el-table
				:data="relationUserList"
				style="width: 100%"
				size="small"
				@selection-change="handleSelectionChangeRelation">
				<el-table-column align="center" type="selection" width="80"></el-table-column>
				<el-table-column
					align="center"
					label="序号"
					type="index"
					width="80"></el-table-column>
				<el-table-column align="center" label="姓名" prop="userName"></el-table-column>
				<el-table-column
					align="center"
					label="部门名称"
					prop="userDeptName"></el-table-column>
				<el-table-column align="center" label="联系方式" prop="userPhone"></el-table-column>
				<el-table-column align="center" label="操作" width="200">
					<template slot-scope="scope">
						<el-button size="mini" type="danger" @click="delSelectUser(scope.row)">
							删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<pagination
				v-show="relationUserTotal > 0"
				:total="relationUserTotal"
				:page.sync="userQueryParams.pageNum"
				:limit.sync="userQueryParams.pageSize"
				:page-sizes="[5, 10, 15]"
				:page-size="5"
				@pagination="getCurrentDeviceRelationUsersList()"
				style="margin-bottom: 20px" />

			<el-dialog
				:visible.sync="open"
				:show-close="false"
				:close-on-click-modal="false"
				:close-on-press-escape="false"
				title="请选择关联人员"
				width="1200px"
				center
				append-to-body>
				<el-transfer
					ref="selectUserTransferRef"
					v-loading="loadingRelationUserData"
					v-model="hasSelectData"
					filterable
					:data="selectData"
					:titles="['人员列表', '本次选择添加人员']"
					:button-texts="['删除', '添加']"
					filter-placeholder="请输入人员名称"></el-transfer>
				<div slot="footer" class="dialog-footer">
					<el-button
						size="mini"
						type="primary"
						:loading="saveRelationUserLoading"
						@click="saveSelectRelationUser">
						确 定
					</el-button>
					<el-button size="mini" type="danger" @click="cancel">取 消</el-button>
				</div>
			</el-dialog>
		</el-dialog>
	</div>
</template>

<script>
import {
	getTaskReceive,
	receiveTask,
	revertTask,
	getDeviceRelationList,
	relationUsers,
	relationDeleteUsers
} from '@/api/business/deviceTask';
import { getFormInfo } from '@/api/business/reqContReview';
import { getProcessList, goProcessTemplate } from '@/api/process/process';
import { getCurrentUserInfo } from '@/api/system/user';
import { isEndByPiId } from '@/api/workflow/workFlowIns/flowInst';
import { selectActiveUser } from '@/api/workflow/workFlowIns/flowInst';

export default {
	data() {
		return {
			user: {},
			// 日期范围
			dateRange: [],
			// 显示搜索条件
			showSearch: true,
			queryParams: {
				pageNum: 1,
				pageSize: 10,
				isRead: '1',
				title: '',
				contractNumber: null,
				isReceiving: null
			},
			receivingStatus: [
				{ statusValue: '1', statusLabel: '未领取' },
				{ statusValue: '0', statusLabel: '已领取' }
			],
			receiveList: [],
			receiveMapList: [],
			receiveMap: undefined,
			total: 0,
			ids: '',
			// 非多个禁用
			multiple: true,
			isRootChange: true,
			rootChangeIds: [],
			processInfo: {},
			sxpzInfo: {},

			// --- 关联人员
			loadingRelationUserData: false,
			relationDialog: false,
			open: false,

			userQueryParams: {
				pageNum: 1,
				pageSize: 5,
				deviceId: ''
			},
			currentDeviceId: '',
			relationUserTotal: 0,
			relationUserList: [],
			multipleUser: true,
			relationRowIds: '',
			selectData: [],
			hasSelectData: [],
			saveRelationUserLoading: false
		};
	},
	methods: {
		// ---------- 关联用户
		/**
		 *  获取当前 deviceId 记录所有关联的 用户信息
		 **/
		getCurrentDeviceRelationUsersList() {
			getDeviceRelationList(this.userQueryParams).then(response => {
				this.relationUserList = response.rows;
				this.relationUserTotal = response.total;
			});
		},
		// 显示 用户信息 dialog
		clickRelationUsers(row) {
			this.relationDialog = true;
			this.userQueryParams.deviceId = row.rowId;
			this.getCurrentDeviceRelationUsersList();
		},
		// 显示 选择人员列表
		clickViewUserLists() {
			this.loadingRelationUserData = true;
			if (this.selectData == undefined || this.selectData.length == 0) {
				this.generateData();
			}
			this.loadingRelationUserData = false;
			this.open = true;
		},
		// 多选框选中数据

		handleSelectionChangeRelation(selection) {
			this.relationRowIds = selection.map(item => item.rowId);
			this.multipleUser = !selection.length;
		},
		saveSelectRelationUser() {
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
			let deviceId = this.userQueryParams.deviceId;
			let selectUserIds = userIds.split(',');
			relationUsers(deviceId, selectUserIds).then(response => {
				if (response.code == 200) {
					this.$message.success('添加成功！');
					this.open = false;
					this.saveRelationUserLoading = false;
					this.hasSelectData = [];
					this.getCurrentDeviceRelationUsersList();
				}
			});
		},
		delSelectUser(row) {
			let ids = (row.rowId == undefined ? null : row.rowId.split(',')) || this.relationRowIds;
			this.$confirm('您确定要删除选中人员吗?', '警告', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(value => {
				relationDeleteUsers(ids).then(response => {
					this.$message.success('操作成功!');
					this.getCurrentDeviceRelationUsersList();
				});
			});
		},
		generateData() {
			var appendUserIds = '';
			this.relationUserList.forEach((value, index, array) => {
				// console.log(this.actUserList[index].userId);
				appendUserIds += this.relationUserList[index].userId + ',';
			});
			appendUserIds = appendUserIds.substring(0, appendUserIds.length - 1);
			let existUserIdArr = appendUserIds.split(',');
			selectActiveUser().then(response => {
				let lineList = response.data;
				for (var i = 0; i < lineList.length; i++) {
					let bean = lineList[i];
					let beanUserName = bean.userId;
					// console.log(beanUserName + '返回索引值为 -- > ' + (this.checkArryIsExistElement(existUserIdArr,beanUserName)));
					this.selectData.push({
						label: bean.deptName + ' ----- ' + bean.nickName,
						key: bean.userId,
						disabled: this.checkArryIsExistElement(existUserIdArr, beanUserName) != -1
					});
				}
				this.open = true;
			});
		},
		clearDeviceId() {
			this.userQueryParams.deviceId = '';
		},
		// 重置选择人员框
		reset() {
			this.$refs.selectUserTransferRef.clearQuery('left');
			this.$refs.selectUserTransferRef.clearQuery('right');
			this.selectData = [];
			this.hasSelectData = [];
		},
		// 取消按钮
		cancel() {
			this.reset();
			this.open = false;
		},
		// ---------------------------------------------------
		getCurrentUser() {
			getCurrentUserInfo().then(response => {
				if (response.code === 200) {
					this.user = response.data;
				}
			});
		},
		getList() {
			this.receiveMapList = [];
			getTaskReceive(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
				this.total = response.total;
				this.receiveList = [];
				this.receiveList = this.dataConvert(response.rows);
				this.receiveMap = {};

				if (this.receiveList.length == 0) {
					return;
				}

				let tempStoreMap = {};
				let tempContractNumber = null;
				// 列表下的 根 数据列表
				let tempContractRootList = [];
				// 当前根数据列表的数据
				let tempContractMappingList = [];

				this.receiveList.forEach((item, index) => {
					tempContractNumber = item.contractNumber;
					this.receiveMap[item.rowId] = item;
					tempContractMappingList = [];
					tempContractRootList = tempStoreMap[tempContractNumber];
					// 当前Map临时存储器中 当前的合同编号对应的数据列表为空
					if (!tempContractRootList) {
						tempContractMappingList = [];
						tempContractRootList = [];

						tempContractMappingList.push(item);
						tempContractRootList.push({
							rowId: item.rowId + index,
							contractNumber: item.contractNumber,
							createTime: item.createTime,
							isMenu: true,
							children: tempContractMappingList
						});
					}
					// 否则就将当前元素装入对应的列表中
					else {
						tempContractMappingList = tempContractRootList[0].children;
						tempContractMappingList.push(item);
						tempContractRootList.children = tempContractMappingList;
					}
					tempStoreMap[tempContractNumber] = tempContractRootList;
				});

				for (const key in tempStoreMap) {
					let requireNodeList = tempStoreMap[key];
					// console.log(requireNodeList[0]);
					this.receiveMapList.push(requireNodeList[0]);
				}
				// console.log(this.receiveMapList);
			});
		},
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
		//type=1领取任务 type=2归还任务
		handTask(type, row) {
			// if(row.paymentType != '0'){
			//   if(row.isPaymentSuccess == '0'){
			//     this.$message.error('当前业务还未确认缴费!不能领取');
			//     return ;
			//   }
			// }
			if (type === 1) {
				if (this.receiveMap[row.rowId].isReceiving == '0') {
					this.msgError('该任务已被领取，请选择其他任务');
					return;
				}
				this.receiveTask(row.rowId);
			} else {
				if (this.receiveMap[row.rowId].isReceiving == '1') {
					this.msgError('暂未领取该任务，不能归还！');
					return;
				}
				this.revertTask(row.rowId);
			}
		},
		handRevert(row) {
			this.ids.push(row.rowId);
			this.handleRevert('暂未领取该任务，不能归还！');
		},
		//一键领取任务
		handReviceAll() {
			var flag = this.judgmentRecevie(
				'0',
				'您选择的任务中存在已领取的任务，请检查后再点击领取'
			);
			if (flag) {
				this.receiveTask(undefined);
			}
		},
		//一键归还任务
		handleRevert() {
			var flag = this.judgmentRecevie(
				'1',
				'您选择的任务中存在未领取的任务，请检查后再点击归还'
			);
			flag = this.judgmentRecevie(
				'0',
				'您选择的任务中存在已完成的任务，请检查后再归还',
				true
			);
			if (flag) {
				this.revertTask(undefined);
			}
		},
		handleSelectionChange(selection) {
			this.ids = selection.map(item => item.rowId);
			this.multiple = !selection.length;
		},
		//判断该任务是否已被领取
		judgmentRecevie(isReceiving, msg, read) {
			var flag = true;
			for (var i = 0; i < this.ids.length; i++) {
				var id = this.ids[i];
				if (read && this.receiveMap[id].isRead == isReceiving) {
					this.msgError(msg);
					flag = false;
					break;
				} else if (this.receiveMap[id].isReceiving == isReceiving) {
					this.msgError(msg);
					flag = false;
					break;
				}
			}
			return flag;
		},
		//发送请求，领取任务
		receiveTask(id) {
			const rowIds = id || this.ids;
			this.$confirm('确认领用吗?', '警告', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			})
				.then(function () {
					return receiveTask(rowIds);
				})
				.then(() => {
					this.getList();
					this.msgSuccess('领用成功');
				})
				.catch(() => {
					this.getList();
				});
		},
		//发送请求，归还任务
		revertTask(id) {
			const rowIds = id || this.ids;
			this.$confirm('确认归还吗?', '警告', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			})
				.then(function () {
					return revertTask(rowIds);
				})
				.then(() => {
					this.getList();
					this.msgSuccess('归还成功');
				})
				.catch(() => {
					this.getList();
				});
		},
		// 复选框是否可用状态判定
		selectChangeStatus(row) {
			if (row.isMenu) {
				return false;
			}
			return true;
		},
		// 多选框选中数据
		handleSelectionChange(selection) {
			this.ids = selection.map(item => item.rowId);
			this.single = selection.length != 1;
			this.multiple = !selection.length;

			let rootIds = '';
			this.receiveMapList.forEach(item => {
				rootIds += item.rowId + ',';
			});
			/*  
              isRootChange:true,
              rootChangeIds:[],
          */
			for (var i = 0; i < this.ids.length; i++) {
				if (rootIds.indexOf(this.ids[i]) == -1) {
					this.isRootChange = false;
					break;
				} else {
					if (i == this.ids.length - 1) {
						// console.log('进行批量操作！');
						this.isRootChange = true;
					}
				}
			}
		},
		//前往检验申请开始页面，处理任务
		handleStartCheck(row) {
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
				if (bean['updateTime'] != null) {
					bean['updateTime'] = bean['updateTime'].replaceAll('T', ' ').split('.')[0];
				}
				retList.push(bean);
			}
			return retList;
		}
	},
	mounted() {
		this.getList();
		this.getCurrentUser();
	}
};
</script>

<style>
.handleBtn {
	margin-left: 15px;
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
</style>
