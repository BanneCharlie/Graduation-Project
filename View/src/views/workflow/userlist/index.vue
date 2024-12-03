<template>
  <el-card class="box-card">
    <el-form :model="queryParams" ref="queryForm" v-show="showSearch" :inline="true">
      <el-form-item label="用户名" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入用户名"
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
        >关联人员</el-button>
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

    <el-table size="medium" style="width: 100%;margin-top: 20px;" :data="actUserList" v-loading="loading"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column type="index" label="序号" align="center" width="610"></el-table-column>
      <el-table-column prop="userName" label="用户名" align="center" width="610"></el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改角色配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body v-loading="userTabLoading">
      <el-transfer
        filterable
        :titles="['人员列表', '本次选择添加人员']"
        :button-texts="['删除', '添加']"
        filter-placeholder="请输入人员名称"
        v-model="hasSelectData"
        :data="selectData">
      </el-transfer>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handelSave">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>

  import { getActUserList, actUsersLookup, actSaveUserToActs, delUser } from "@/api/workflow/userList";

  export default {
    name: "actMgt",
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
          userName: undefined,
          procDefId: undefined,
          actDefId: undefined
        },
        // 日期范围
        dateRange: [],
        // 显示搜索条件
        showSearch: true,
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        formItem: {
          enId: '',
          orgName: '',
          orgId: '',
          userName: '',
          userId:'',
          actDefName:'',
          procDefId:'',
          actDefId:'',
          enStatus:'',
          modifyUserId:'',
          createUserId:'',
          verCode:'',
          verNum:'',
          enType:'',
          enOrder:''
        },
        // 表单校验
        rules: {
          actName: [
            { required: true, message: "流程定义名称不能为空", trigger: "blur" }
          ],
          actDefId: [
            { required: true, message: "流程定义ID不能为空", trigger: "blur" }
          ]
        },
        actUserList: [],
        lineList: [],
        selectData: [],
        hasSelectData: [],
        submitData: {
          actId: '',
          procDefId: '',
          userIds: '',
        },
        userTabLoading: false,
      }
    },
    methods: {
      getList() {
        this.loading = true;
        getActUserList(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
            this.actUserList = response.rows;
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
      handelSave() {
        this.userTabLoading = true;
        var userIds = '';
        for(var i = 0; i < this.hasSelectData.length; i++) {
          userIds += this.hasSelectData[i] + ',';
        }
        this.submitData.actId = this.actDefId
        this.submitData.procDefId = this.procDefId
        this.submitData.userIds = userIds.substring(0, userIds.length - 1)
        actSaveUserToActs(this.submitData).then(response => {
          this.msgSuccess("添加成功");
          this.open = false;
          this.getList();
          this.userTabLoading = false;
        })
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset()
        this.generateData()
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const enIds = row.enId || this.ids;
        this.$confirm('是否确认删除该数据?', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(function() {
             return delUser(enIds);
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
        this.selectData = []
        this.hasSelectData = []
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
      generateData() {
        var appendUserIds = '';
          this.actUserList.forEach((value, index, array) => {
            // console.log(this.actUserList[index].userId);
            appendUserIds += this.actUserList[index].userId + ',';
          })
          appendUserIds = appendUserIds.substring(0,appendUserIds.length-1);
        let existUserIdArr = appendUserIds.split(',')
        actUsersLookup().then(response => {
            this.lineList = response.rows;
            for(var i = 0; i < this.lineList.length; i++) {
              let bean = this.lineList[i]
              let beanUserName = bean.userId;
              // console.log(beanUserName + '返回索引值为 -- > ' + (this.checkArryIsExistElement(existUserIdArr,beanUserName)));
              this.selectData.push({
                label: bean.nickName,
                key: bean.userId,
                disabled: ((this.checkArryIsExistElement(existUserIdArr,beanUserName)) != -1)
              });
            }
            this.title = "请选择人员";
            this.open = true;
          }
        );

      },

    },
    mounted: function() {
      this.actDefId = this.$route.params.actDefId
      this.procDefId = this.$route.params.procDefId
      this.queryParams.actDefId = this.$route.params.actDefId
      this.queryParams.procDefId = this.$route.params.procDefId
      this.getList()
    }
  }
</script>

<style>
    .el-transfer-panel__list.is-filterable{
    width:320px !important;
    height:380px !important;
    }
  .el-transfer-panel{
      margin-left: 30px;
      width: 320px !important;
      height: 480px !important;
  }
</style>
