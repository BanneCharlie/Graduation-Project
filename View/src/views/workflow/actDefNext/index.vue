<template>
	<el-card class="box-card">
		<el-form :model="queryParams" ref="queryForm" v-show="showSearch" :inline="true">
			<el-form-item label="节点名称" prop="actName">
				<el-input
					v-model="queryParams.actName"
					placeholder="请输入流程节点名称"
					clearable
					size="small"
					style="width: 240px"
					@keyup.enter.native="handleQuery" />
			</el-form-item>
			<el-form-item>
				<el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
				<el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
			</el-form-item>
		</el-form>

		<el-row :gutter="10" class="mb8">
			<el-col :span="1.5">
				<el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">关联节点</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button>
			</el-col>
			<!-- <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </el-col> -->
			<right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
		</el-row>

		<el-table size="medium" style="width: 100%; margin-top: 20px" :data="nextList" v-loading="loading" @selection-change="handleSelectionChange">
			<el-table-column type="selection" width="50" align="center" />
			<el-table-column type="index" label="序号" align="center" width="100"></el-table-column>
			<el-table-column prop="actName" label="节点名称" align="center" width="250"></el-table-column>
			<el-table-column prop="nodeLineName" label="连线名称" align="center"></el-table-column>
			<el-table-column prop="actDefId" label="节点ID" align="center" width="120"></el-table-column>
			<el-table-column label="操作" width="160" align="center">
				<template slot-scope="scope">
					<el-button size="mini" type="primary" @click="handleShowNodeLineName(scope.row)">更新连线名称</el-button>
				</template>
			</el-table-column>
		</el-table>

		<pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

		<!-- 添加或修改角色配置对话框 -->
		<el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
			<el-transfer
				filterable
				:titles="['节点列表', '已添加节点']"
				:button-texts="['删除', '添加']"
				filter-placeholder="请输入节点名称"
				v-model="hasSelectData"
				:data="selectData"></el-transfer>
			<div slot="footer" class="dialog-footer">
				<el-button type="primary" @click="handelSave">确 定</el-button>
				<el-button @click="cancel">取 消</el-button>
			</div>
		</el-dialog>

		<!-- 显示修改连线名称的dialog组件 -->
		<el-dialog
			title="修改连线名称"
			:visible.sync="showNodeLineNameDialog"
			width="40%"
			append-to-body
			:close-on-click-modal="false"
			:show-close="false"
			center>
			<el-form :model="nodeFormData" label-suffix=":" label-width="150px" size="mini">
				<el-form-item label="节点连线名称">
					<el-input v-model="nodeFormData.nodeLineName" placeholder="请输入节点连线名称"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button size="mini" type="success" @click="submitUpdateNodeLineName">提交数据</el-button>
				<el-button size="mini" @click="closeUpdateNodeLineNameDialog">取消</el-button>
			</div>
		</el-dialog>
	</el-card>
</template>

<script>
import { listNextMgt, saveNext, delNext, actLineLookup, actSaveLineToActs, updateNodeLineName } from '@/api/workflow/actDefNext';

export default {
	name: 'actMgt',
	data() {
		return {
			actDefId: undefined,
			procDefId: undefined,
			loading: true,
			// 选中数组
			ids: [],
			// 非单个禁用
			single: true,
			// 非多个禁用
			multiple: true,
			// 总条数
			total: 0,
			// 查询参数
			queryParams: {
				pageNum: 1,
				pageSize: 10,
				actName: undefined,
				procDefId: undefined,
				actDefId: undefined
			},
			// 日期范围
			dateRange: [],
			// 显示搜索条件
			showSearch: true,
			// 弹出层标题
			title: '',
			// 是否显示弹出层
			open: false,
			formItem: {
				enId: '',
				actDefRelId: '',
				actName: '',
				actDefId: '',
				enStatus: '',
				modifyUserId: '',
				createUserId: '',
				verCode: '',
				verNum: '',
				enType: '',
				procDefId: '',
				enOrder: ''
			},
			// 表单校验
			rules: {
				actName: [{ required: true, message: '流程定义名称不能为空', trigger: 'blur' }],
				actDefId: [{ required: true, message: '流程定义ID不能为空', trigger: 'blur' }]
			},
			nextList: [],
			lineList: [],
			selectData: [],
			hasSelectData: [],
			submitData: {
				actId: '',
				procDefId: '',
				actDefId: ''
			},

			// 节点连线名称
			showNodeLineNameDialog: false,
			nodeFormData: {
				nodeLineName: null
			}
		};
	},
	methods: {
		getList() {
			this.loading = true;
			listNextMgt(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
				this.nextList = response.rows;
				this.total = response.total;
				this.loading = false;
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
		/** 提交按钮 */
		handelSave() {
			var actDefId = '';
			for (var i = 0; i < this.hasSelectData.length; i++) {
				actDefId += this.hasSelectData[i] + ',';
			}
			this.submitData.actId = this.actDefId;
			this.submitData.procDefId = this.procDefId;
			this.submitData.actDefId = actDefId.substring(0, actDefId.length - 1);
			actSaveLineToActs(this.submitData).then(response => {
				this.msgSuccess('添加成功');
				this.open = false;
				this.getList();
			});
		},
		/** 新增按钮操作 */
		handleAdd() {
			this.reset();
			this.generateData();
		},
		/** 修改按钮操作 */
		handleUpdate(row) {
			this.reset();
			const enId = row.enId || this.ids;
			getActProc(enId).then(response => {
				this.form = response.data;
				this.open = true;
				this.title = '修改流程定义';
			});
		},
		/** 删除按钮操作 */
		handleDelete(row) {
			const enIds = row.enId || this.ids;
			this.$confirm('是否确认删除该数据?', '警告', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			})
				.then(function () {
					return delNext(enIds);
				})
				.then(() => {
					this.getList();
					this.msgSuccess('删除成功');
				});
		},
		/** 导出按钮操作 */
		handleExport(row) {
			const enIds = row.enId || this.ids;
			this.$confirm('是否确认导出所有角色数据项?', '警告', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			})
				.then(function () {
					return exportActProc(enIds);
				})
				.then(response => {
					this.download(response.msg);
				});
		},
		// 表单重置
		reset() {
			this.selectData = [];
			this.hasSelectData = [];
		},
		// 多选框选中数据
		handleSelectionChange(selection) {
			this.ids = selection.map(item => item.enId);
			this.single = selection.length != 1;
			this.multiple = !selection.length;
		},
		// 取消按钮
		cancel() {
			this.open = false;
			this.reset();
		},
		generateData() {
			actLineLookup(this.procDefId).then(response => {
				this.lineList = response.rows;
				for (var i = 0; i < this.lineList.length; i++) {
					var bean = this.lineList[i];
					this.selectData.push({
						label: bean.actName,
						key: bean.enId
					});
				}
				this.title = '关联节点';
				this.open = true;
			});
		},

		// 1. 点击显示更改节点连线名称的数据
		handleShowNodeLineName(row) {
			Object.assign(this.nodeFormData, row);
			this.showNodeLineNameDialog = true;
		},
		submitUpdateNodeLineName() {
			if (!this.nodeFormData.nodeLineName) {
				this.$message.error('连线名称为空!');
				return;
			}

			updateNodeLineName(this.nodeFormData).then(response => {
				this.$message.success('修改成功!');
				this.closeUpdateNodeLineNameDialog();
				this.getList();
			});
		},
		closeUpdateNodeLineNameDialog() {
			this.showNodeLineNameDialog = false;
		}

		// ====== METHODS end ======
	},
	mounted: function () {
		this.actDefId = this.$route.params.actDefId;
		this.procDefId = this.$route.params.procDefId;
		this.queryParams.actDefId = this.$route.params.actDefId;
		this.queryParams.procDefId = this.$route.params.procDefId;
		this.getList();
	}
};
</script>

<style></style>
