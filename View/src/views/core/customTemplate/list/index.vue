<template>
	<div class="app-container">
		<el-form :model="queryParams" ref="queryForm" v-show="showSearch" :inline="true" size="mini" label-suffix=":">
			<el-form-item label="模板名称" prop="templateName">
				<el-input v-model="queryParams.templateName" placeholder="请输入名称" clearable style="width: 200px" @keyup.enter.native="handleQuery" />
			</el-form-item>
			<el-form-item label="模板类别" prop="templateCategory">
				<el-select v-model="queryParams.templateCategory" placeholder="请选择">
					<el-option v-for="dict in deviceType" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue"></el-option>
				</el-select>
			</el-form-item>
			<el-form-item>
				<el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
				<el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
			</el-form-item>
		</el-form>

		<el-row :gutter="20" class="mb8">
			<el-col :span="1.5">
				<el-button size="mini" type="primary" icon="el-icon-circle-plus-outline" @click="handleModalAdd">添加</el-button>
			</el-col>
			<el-col :span="1.5">
				<el-button size="mini" type="danger" icon="el-icon-delete" :disabled="multiple" @click="handleRemove">删除</el-button>
			</el-col>
			<right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
		</el-row>

		<el-table :data="data" v-loading="loading" size="small" @selection-change="handleSelectionChange">
			<el-table-column type="selection" width="80" align="center" />
			<el-table-column prop="templateName" label="模板名称" align="center"></el-table-column>
			<el-table-column prop="orderNum" label="序号" align="center" width="80"></el-table-column>
			<el-table-column prop="templateCategory" label="模板种类" align="center" width="100"></el-table-column>
			<el-table-column prop="createTime" label="创建时间" align="center" width="150"></el-table-column>
			<el-table-column prop="updateTime" label="修改时间" align="center" width="150"></el-table-column>
			<el-table-column prop="createUserName" label="创建人" align="center" width="110"></el-table-column>
			<el-table-column label="操作" align="center" width="200">
				<template slot-scope="scope">
					<el-button class="handleBtn" type="text" @click="handleModalUpdate(scope.row)" size="mini" icon="el-icon-edit-outline">编辑</el-button>
					<el-button class="handleBtn" @click="handleClick('view', scope.row)" type="text" size="mini" icon="el-icon-s-promotion"  style="display: none;">预览</el-button>
					<el-dropdown @command="handleClick($event, scope.row)" trigger="click" size="mini">
						<el-button class="handleBtn" type="text" size="mini">
							更多
							<i class="el-icon-arrow-down el-icon--right"></i>
						</el-button>
						<el-dropdown-menu slot="dropdown" :split-button="true">
							<el-dropdown-item command="edit_generic_content">配置模板内容</el-dropdown-item>
							<el-dropdown-item command="remove">删除</el-dropdown-item>
						</el-dropdown-menu>
					</el-dropdown>
				</template>
			</el-table-column>
		</el-table>
		<pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

		<el-dialog
			:title="modalTitle"
			:visible.sync="open"
			:inline="true"
			width="50%"
			center
			append-to-body
			:show-close="false"
			:close-on-press-escape="false"
			:close-on-click-modal="false">
			<el-form ref="formItem" label-suffix=":" label-width="200px" size="small" :model="formItem" :rules="formItemRules">
				<el-form-item label="模板名称" prop="templateName">
					<el-input style="width: 400px" v-model="formItem.templateName" placeholder="请输入内容"></el-input>
				</el-form-item>
				<el-form-item label="模板类别" prop="templateCategory">
					<el-select style="width: 400px" v-model="formItem.templateCategory" placeholder="请选择">
						<el-option v-for="dict in deviceType" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="排序" prop="orderNum">
					<el-input-number style="width: 400px" v-model="formItem.orderNum" :min="1" ></el-input-number>
				</el-form-item>
			</el-form>
			<span style="margin-top: 30px" slot="footer" class="dialog-footer">
				<el-button size="mini" type="primary" @click="submitForm">确 定</el-button>
				<el-button size="mini" type="danger" @click="handleReset">取 消</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
import { list, saveReport, delReport } from '@/api/customTemplate/reportTemplate';
import { getAll } from '@/api/customTemplate/dataSourceConifg';

export default {
	name:'CustomTemplateList',
	data() {
		return {
			// 总条数
			total: 0,
			loading: false,
			open: false,
			modalTitle: '',
			spinShow: false,
			isEdit: false,
			jdbcDriverSelect: [],
			dateRange: [],
			// 查询参数
			queryParams: {
				pageNum: 1,
				pageSize: 10,
				dataSourceName: undefined,
				createTime: undefined,
				templateCategory: undefined
			},
			formItemRules: {
				templateName: [
					{
						required: true,
						message: '不能为空',
						trigger: 'blur'
					}
				],
				templateType: [
					{
						required: true,
						message: '不能为空',
						trigger: 'blur'
					}
				],
				dataSourceName: [
					{
						required: true,
						message: '不能为空',
						trigger: 'blur'
					}
				]
			},
			formItem: {
				templateId: '',
				templateName: '',
				templateType: '',
				dataSourceId: '',
				dataSourceName: '',
				templateCategory: '',
				orderNum: 0
			},
			data: [],
			// 设备种类 从 合同评审中获得值
			deviceType: [],
			// 显示搜索条件
			showSearch: true,
			// 非多个禁用
			multiple: true
		};
	},
	created() {
		this.getDeviceCateDict().then(response => {
			this.deviceType = response.data;
		});
	},
	methods: {
		getList() {
			this.loading = true;
			list(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
				this.data = response.rows;
				this.total = response.total;
				setTimeout(() => {
          this.loading = false;
        }, 300);
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
		// 多选框选中数据
		handleSelectionChange(selection) {
			this.ids = selection.map(item => item.templateId);
			this.single = selection.length !== 1;
			this.multiple = !selection.length;
		},
		handleModalAdd() {
			this.handleSearchDataSource();
			this.handleReset();
			this.modalTitle = '添加报告模板';
			this.open = true;
		},
		handleModalUpdate(data) {
			this.handleSearchDataSource();
			this.modalTitle = '编辑报告模板';
			this.formItem = Object.assign({}, this.formItem, data);
			this.isEdit = true;
			this.open = true;
		},
		handleResetForm(form) {
			this.$refs[form].resetFields();
		},
		handleReset() {
      this.resetForm('formItem');
			this.isEdit = false;
			this.open = false;
			this.jdbcDriverSelect = [];
		},
		submitForm: function () {
			let msg = '';
			this.$refs['formItem'].validate(valid => {
				if (valid) {
					if (this.formItem.templateId !== undefined) {
						msg = '添加成功';
					} else {
						msg = '修改成功';
					}
					saveReport(this.formItem).then(response => {
						this.msgSuccess(msg);
						this.open = false;
						this.getList();
					});
				}
			});
		},
		handleSearchDataSource() {
			getAll().then(response => {
				this.jdbcDriverSelect = response.data;
			});
		},
		handleRemove(row) {
			const rowIds = row.templateId || this.ids;
			this.$confirm('确认删除吗?', '警告', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			})
				.then(function () {
					return delReport(rowIds);
				})
				.then(() => {
					this.getList();
					this.msgSuccess('删除成功');
				});
		},
		handleClick(name, row) {
			switch (name) {
				case 'remove':
					this.handleRemove(row);
					break;
				case 'edit_generic_content':
					this.$router.push({
            name: 'reportTempletGenericContent',
            params: {
              templateId: row.templateId,
              reportId: '111111',
            }
          });
					break;
				case 'view':
					let param = '?templateId=' + row.templateId;
					let filePath = '/static/template/preview.html';
					let targerUrl = 'http://' + window.location.host + filePath + param;
					window.open(targerUrl);
					break;
			}
		}
	},
	mounted: function () {
		this.getList();
	}
};
</script>
<style>
.handleBtn {
	margin-left: 15px;
}
.pagination-container{
  height: 45px;
}
</style>
