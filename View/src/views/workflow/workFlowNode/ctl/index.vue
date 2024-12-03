<template>
    <el-card class="box-card">
      <el-form :model="queryParams" ref="queryForm" v-show="showSearch" :inline="true">
        <el-form-item label="流程实例标题" prop="fromTitle">
          <el-input
            v-model="queryParams.fromTitle"
            placeholder="流程实例标题"
            clearable
            size="small"
            style="width: 240px"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
  
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
          >添加流程实例</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="danger"
            plain
            icon="el-icon-delete"
            size="mini"
            :disabled="multiple"
            @click="handleDelete"
          >批量删除</el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>
  
      <el-table size="medium" style="width: 100%;margin-top: 20px;" :data="data" v-loading="loading"
                @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column type="index" label="序号" align="center" width="50"></el-table-column>
        <el-table-column prop="enId" label="流程实例ID" align="center"></el-table-column>
        <el-table-column prop="procName"  label="流程名称" align="center" width="220"></el-table-column>
        <el-table-column prop="enStatus"  label="状态	" align="center" width="130"></el-table-column>
        <el-table-column prop="createUserId" label="创建人" align="center" width="160"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" align="center" width="160"></el-table-column>
        <el-table-column label="操作" align="center" width="300">
              <template slot-scope="scope">
                  <el-button class="handleBtn" type="primary" size="small" @click="handleUpdate(scope.row)">编辑流程说明</el-button>
                  <el-button class="handleBtn" type="primary" size="small" @click="handleClick(scope.row)">编辑流程实例节点</el-button>
              </template>
        </el-table-column>
      </el-table>
  
      <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.page"
        :limit.sync="queryParams.limit"
        @pagination="getList"
      />
  
      <el-dialog :title="title" :visible.sync="open" width="650px" append-to-body>
        <el-form ref="form" :model="form" :rules="rules" label-width="120px" label-suffix=":" label-position="right">
            <el-form-item hidden prop="enId">
                <el-input :disabled="true" v-model="form.enId" />
            </el-form-item>
          <el-form-item label="流程名" prop="procName">
            <el-input v-model="form.procName" placeholder="请输入流程名" />
          </el-form-item>
          <el-form-item label="流程实例ID" prop="procInstId">
            <el-input v-model="form.procInstId" placeholder="请输入流程实例ID" />
          </el-form-item>
          <el-form-item label="流程说明" prop="procDesc">
            <el-input v-model="form.procDesc" placeholder="请输入流程定义备注" />
          </el-form-item>
          <el-form-item label="当前环节名称" prop="currActName">
            <el-input v-model="form.currActName" placeholder="请输入当前环节名称" />
          </el-form-item>
          <el-form-item label="当前环节ID" prop="currActId">
            <el-input v-model="form.currActId" placeholder="请输入当前环节ID" />
          </el-form-item>
          <el-form-item label="状态" prop="enStatus">
            <el-input v-model="form.enStatus" placeholder="状态" />
          </el-form-item>
          <el-form-item label="表单类别" prop="fromType">
            <el-input v-model="form.fromType" placeholder="表单类别" />
          </el-form-item>
          <el-form-item label="流程实例名称" prop="fromTitle">
            <el-input v-model="form.fromTitle" placeholder="流程实例名称" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </el-dialog>
    </el-card>
  </template>
  
  <script>
    import { 
        getAllFlowInst, 
        editFlowInst, 
        removeFlowInst, 
        saveFlowInst, 
        flowInstNode,

        editInstNode,
        saveInstNode,
        removeInstNode,
        flowInstNodeActiveUserList,
        currentFlowInstNodeUser,
        saveWorkItem,
        removeFlowInstNodeUser,
        selectActiveUser
    } from "@/api/workflow/workFlowIns/flowInst";
  
    export default {
      name: "engineMgt",
      data() {
        return {
          loading: true,
          engineMgtList: null,
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
            page: 1,
            limit: 10,
            fromTitle: undefined
          },
          // 日期范围
          dateRange: [],
          // 显示搜索条件
          showSearch: true,
          // 弹出层标题
          title: "流程实例",
          // 是否显示弹出层
          open: false,
          // 表单参数
          form: {},
          // 表单校验
          rules: {
            procName: [
              { required: true, message: "流程名称不能为空", trigger: "blur" }
            ],
            procDesc: [
              { required: true, message: "流程说明不能为空", trigger: "blur" }
            ]
          },
          data: []
        }
      },
      methods: {
        getList() {
          this.loading = true;

          getAllFlowInst(
              this.queryParams
              ).then(response => {
              this.data = response.data.records;
              this.total = parseInt(response.data.total);
              this.loading = false;
            }
          );
        },
        /** 搜索按钮操作 */
        handleQuery() {
          this.queryParams.page = 1;
          this.getList();
        },
        /** 重置按钮操作 */
        resetQuery() {
          this.dateRange = [];
          this.resetForm("queryForm");
          this.handleQuery();
        },
        /** 提交按钮 */
        submitForm: function() {
          this.$refs["form"].validate(valid => {
            if (valid) {
              if (this.form.enId != undefined) {
                saveFlowInst(this.form).then(response => {
                  this.msgSuccess("修改成功");
                  this.open = false;
                  this.getList();
                });
              } else {
                saveFlowInst(this.form).then(response => {
                  this.msgSuccess("新增成功");
                  this.open = false;
                  this.getList();
                });
              }
            }
          });
        },
        /** 新增按钮操作 */
        handleAdd() {
          this.reset();
          this.open = true;
          this.title = "添加流程实例";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
          this.reset();
          console.log(row.enId);
        //   debugger;
          editFlowInst({
              rowId:row.enId
          }).then(response => {
            this.form = response.data;
            this.open = true;
            this.title = "修改流程实例";
          });
        },
        /** 删除按钮操作 */
        handleDelete() {
            const delids = this.ids.join(',');
            console.log(delids);
          this.$confirm('是否确认删除该数据?', "警告", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning"
            }).then(function() {
              return removeFlowInst({
                delids:delids
              });
            }).then(() => {
              this.getList();
              this.msgSuccess("删除成功");
            })
        },
        // 表单重置
        reset() {
          this.form = {
            procName: undefined,
            procInstId: undefined,
            procDesc: undefined
          };
          this.resetForm("form");
        },
        // 多选框选中数据
        handleSelectionChange(selection) {
          this.ids = selection.map(item => item.enId)
          this.single = selection.length!=1
          this.multiple = !selection.length
        //   console.log(this.ids.join(','));
        },
        // 取消按钮
        cancel() {
          this.open = false;
          this.reset();
        },
        handleClick(row) {
          this.$router.push({name:'instNode', params:{procInstId: row.enId}});
        }
      },
      mounted: function() {
        this.getList()
      }
    }
  </script>
  
  <style>
  </style>
  