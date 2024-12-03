<template>

  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" v-show="showSearch" :inline="true">
      <el-form-item label="数据源名称" prop="dataSourceName">
        <el-input v-model="queryParams.dataSourceName" placeholder="请输入数据源名称" clearable size="small" style="width: 240px"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-circle-plus-outline" @click="handleAdd">添加</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dataSourceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="80" align="center" />
      <el-table-column label="数据源名称" align="center" prop="dataSourceName" width="180" />
      <el-table-column label="驱动类型" align="center" prop="jdbcDriverType" width="180" />
      <el-table-column label="表空间(库)" align="center" prop="schemaName" width="180" />
      <el-table-column label="是否启用" align="center" prop="status" width="180">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.status"
                     active-color="#13ce66" inactive-color="#ff4949"
                     active-value="active" inactive-value="disable"
                     style="margin-left: 10px;"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseUTCTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="primary" icon="el-icon-edit-outline" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="warning" icon="el-icon-unlock" @click="handleUpdatePassword(scope.row)">修改密码</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open"  width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="数据源名称:" prop="dataSourceName">
          <el-input v-model="form.dataSourceName" placeholder="请输入数据源名称" />
        </el-form-item>
        <el-form-item label="URL:" prop="jdbcUrl">
          <el-input v-model="form.jdbcUrl" placeholder="请输入URL" />
        </el-form-item>
        <el-form-item v-if="form.dataSourceId == undefined" label="用户名:" prop="jdbcUserName">
          <el-input v-model="form.jdbcUserName" placeholder="请输入用户名" style="width: 200px;" />
        </el-form-item>
        <el-form-item v-if="form.dataSourceId == undefined" label="密码:" prop="jdbcPwd">
          <el-input v-model="form.jdbcPwd" placeholder="请输入密码" show-password style="width: 200px;" />
        </el-form-item>
        <el-form-item label="驱动类型:" prop="jdbcDriverType">
          <el-select v-model="form.jdbcDriverType" placeholder="请选择" style="width: 200px;" >
            <el-option
              v-for="item in driverTypes"
              :key="item.value"
              :label="item.name"
              :value="item.value">
            </el-option>
           </el-select>
        </el-form-item>
        <el-form-item label="表空间(库):" prop="schemaName">
          <el-input v-model="form.schemaName" placeholder="请输入流程定义备注" style="width: 200px;" />
        </el-form-item>
        <el-form-item label="是否启用:" prop="status">
          <el-switch v-model="form.status"
                     active-color="#13ce66" inactive-color="#ff4949"
                     active-value="active" inactive-value="disable"
                     style="margin-left: 10px;"></el-switch>
        </el-form-item>
        <el-form-item label="排序:" prop="orderNum">
          <el-input v-model="form.orderNum" placeholder="请输入流程定义备注" style="width: 200px;" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="pwdTitle" :visible.sync="pwdOpen"  width="500px" append-to-body>
      <el-form :ref="passwordForm" :model="passwordForm" :rules="pwdRules" label-width="120px">
        <el-form-item label="数据源原密码" prop="originalPwd">
          <el-input v-model="passwordForm.originalPwd" show-password placeholder="请输入原密码" />
        </el-form-item>
        <el-form-item label="数据源新密码:" prop="newPwd">
          <el-input v-model="passwordForm.newPwd" show-password placeholder="请输入新密码" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="confirmPwd">确 定</el-button>
        <el-button @click="cancelPwd">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    list, saveDataSource, delDataSource, getDataSourceById, updatePassword
  } from "@/api/customTemplate/dataSourceConifg";

  export default {
    name: "DataSource",
    data() {
      return {
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        // 数据源列表
        dataSourceList: [],
        // 弹出层标题
        title: "",
        //密码弹出框标题
        pwdTitle: '',
        // 是否显示弹出层
        open: false,
        // 日期范围
        pwdOpen: false,
        dateRange: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          dataSourceName: undefined,
          createTime: undefined
        },
        // 表单参数
        form: {},
        rules: {
          dataSourceName: [
            { required: true, message: "数据源名称不能为空", trigger: "blur" }
          ],
          jdbcUrl: [
            { required: true, message: "URL不能为空", trigger: "blur" }
          ],
          jdbcUserName: [
            { required: true, message: "用户名不能为空", trigger: "blur" }
          ],
          jdbcPwd: [
            { required: true, message: "密码ID不能为空", trigger: "blur" }
          ],
          jdbcDriverType: [
            { required: true, message: "驱动类型不能为空", trigger: "blur" }
          ],
          schemaName: [
            { required: true, message: "表空间名称不能为空", trigger: "blur" }
          ]
        },
        driverTypes: [{
          name: 'MySQL',
          value: 'mysql'
        }, {
          name: 'ORACLE',
          value: 'oracle'
        }],
        passwordForm: {
          rowId: '',
          originalPwd: '',
          newPwd: ''
        },
        pwdRules: {
          originalPwd: [
            { required: true, message: "原密码不能为空", trigger: "blur" }
          ],
          newPwd: [
            { required: true, message: "新密码不能为空", trigger: "blur" }
          ]
        },
      };
    },
    created() {
      this.getList();
    },
    methods: {
      /** 查询委托合同评审列表 */
      getList() {
        this.loading = true;
        list(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
            this.dataSourceList = response.rows;
            this.total = response.total;
            this.loading = false;
          }
        );
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.dateRange = [];
        this.resetForm("queryForm");
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.dataSourceId)
        this.single = selection.length != 1
        this.multiple = !selection.length
      },
      // 表单重置
      reset() {
        this.form = {
          dataSourceId: undefined,
          dataSourceName: undefined,
          jdbcUrl: undefined,
          jdbcUserName: undefined,
          jdbcPwd: undefined,
          jdbcDriverType: undefined,
          schemaName: undefined,
          status: 'active',
          orderNum: undefined
        };
        this.resetForm("form");
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加数据源配置";
      },
      /** 编辑按钮操作 */
      handleEdit(row) {
        this.reset();
        const rowId = row.dataSourceId
        getDataSourceById(rowId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改数据源配置";
        });
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.dataSourceId != undefined) {
              saveDataSource(this.form).then(response => {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              saveDataSource(this.form).then(response => {
                this.msgSuccess("添加成功");
                this.open = false;
                this.getList();
              });
            }
          }
        });
      },
      //删除按钮操作
      handleDelete(row) {
        const rowIds = row.dataSourceId || this.ids;
        this.$confirm('确认删除吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delDataSource(rowIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
      },
      parseUTCTime(time) {
        //2021-05-31T14:23:22.000+0800
        return time.replaceAll("T", " ").split(".")[0]
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },

      //打开修改密码输入框
      handleUpdatePassword(row) {
        this.resetPwd();
        this.pwdOpen = true;
        this.pwdTitle = "修改数据源密码";
        this.passwordForm.rowId = row.dataSourceId;
      },
      //提交原密码与修改的密码
      confirmPwd() {
        updatePassword(this.passwordForm).then(response => {
          this.msgSuccess("修改成功");
          this.pwdOpen = false;
        });
      },
      //关闭修改密码输入框
      cancelPwd() {
        this.pwdOpen = false;
        this.resetPwd();
      },
      //重置修改密码输入框
      resetPwd() {
        this.passwordForm = {
          rowId: undefined,
          originalPwd: undefined,
          newPwd: undefined,
        };
        this.resetForm("form");
      }
    }
  };
</script>

<style>
</style>
