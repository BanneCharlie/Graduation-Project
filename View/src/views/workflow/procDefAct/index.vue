<template>
  <el-card class="box-card">
    <el-form :model="queryParams" ref="queryForm" v-show="showSearch" :inline="true">
      <el-form-item label="流程节点名称" prop="actName">
        <el-input
          v-model="queryParams.actName"
          placeholder="请输入流程节点名称"
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
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >删除</el-button>
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

    <el-table size="medium" style="width: 100%;margin-top: 20px;" :data="data" v-loading="loading"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column type="index" label="序号" align="center" width="50"></el-table-column>
      <el-table-column prop="actDefId" label="节点定义ID" align="center" width="210"></el-table-column>
      <el-table-column prop="actName" label="节点定义名称" align="center"  width="210"></el-table-column>
      <el-table-column prop="enType"  label="节点类别	" align="center" width="130"></el-table-column>
      <el-table-column prop="procDefId" label="所属流程ID" align="center" width="210" ></el-table-column>
      <el-table-column prop="enOrder" label="次序" align="center" width="50"></el-table-column>
      <el-table-column label="操作" align="center" width="370">
            <template slot-scope="scope">
                <el-button class="handleBtn" type="primary" size="small" @click="handleClick('next',scope.row)">编辑节点下一步</el-button>
                <el-button class="handleBtn" type="primary" size="small" @click="handleClick('users',scope.row)">节点参与者</el-button>
            </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改角色配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="节点名称" prop="actName">
          <el-input v-model="form.actName" placeholder="请输入流程定义名称" />
        </el-form-item>
        <el-form-item label="节点ID" prop="actDefId">
          <el-input v-model="form.actDefId" placeholder="请输入流程定义ID" />
        </el-form-item>
        <el-form-item label="流程定义ID" prop="procDefId">
          <el-input v-model="form.procDefId" placeholder="请输入流程定义ID" disabled/>
        </el-form-item>
        <el-form-item label="节点类别" prop="enType">
          <el-select v-model="form.enType" placeholder="请选择">
            <el-option
              v-for="item in enTypes"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
           </el-select>
        </el-form-item>
        <el-form-item label="节点顺序" prop="enOrder">
          <el-input v-model="form.enOrder" placeholder="请输入流程定义备注" />
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

  import { listActMgt, exportActProc, getActProc, saveActProc, delActProc } from "@/api/workflow/actProc";

  export default {
    name: "actMgt",
    data() {
      return {
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
          procDefId: undefined
        },
        // 日期范围
        dateRange: [],
        // 显示搜索条件
        showSearch: true,
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        enTypes: [{
          value: 'act',
          label: '人工节点'
        }, {
          value: 'start',
          label: '开始节点'
        }, {
          value: 'end',
          label: '结束节点'
        }],
        formItem: {
          enId: '',
          actDefId: '',
          actName: '',
          inActsId: '',
          outActsId:'',
          enStatus:'',
          modifyUserId:'',
          createUserId:'',
          verCode:'',
          verNum:'',
          enType:'',
          procDefId:'',
          enOrder:''
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          actName: [
            { required: true, message: "流程定义名称不能为空", trigger: "blur" }
          ],
          actDefId: [
            { required: true, message: "流程定义ID不能为空", trigger: "blur" }
          ]
        },
        data: []
      }
    },
    methods: {
      getList() {
        this.loading = true;
        listActMgt(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
            this.data = response.rows;
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
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.enId != undefined) {
              saveActProc(this.form).then(response => {
                this.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              saveActProc(this.form).then(response => {
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
        this.title = "添加流程定义";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const enId = row.enId || this.ids
        getActProc(enId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改流程定义";
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const enIds = row.enId || this.ids;
        this.$confirm('是否确认删除该数据?', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(function() {
            return delActProc(enIds);
          }).then(() => {
            this.getList();
            this.msgSuccess("删除成功");
          })
      },
      /** 导出按钮操作 */
      handleExport(row) {
        const enIds = row.enId || this.ids;
        this.$confirm('是否确认导出所有角色数据项?', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(function() {
            return exportActProc(enIds);
          }).then(response => {
            this.download(response.msg);
          })
      },
      // 表单重置
      reset() {
        this.form = {
          actName: undefined,
          actDefId: undefined,
          procDefId: this.procDefId,
          enType: undefined,
          enOrder: undefined,
        };
        this.resetForm("form");
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.enId)
        this.single = selection.length!=1
        this.multiple = !selection.length
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      handleClick(type, row) {
        switch (type) {
          case 'next':
            this.$router.push({name:'editActDefNext', params:{actDefId: row.actDefId, procDefId: row.procDefId}});
            break
          case 'users':
            this.$router.push({name:'userList', params:{actDefId: row.actDefId, procDefId: row.procDefId}});
            break
        }
      },
    },
    mounted: function() {
      this.procDefId = this.$route.params.procDefId
      this.queryParams.procDefId = this.$route.params.procDefId
      this.getList()
    }
  }
</script>

<style>
</style>
