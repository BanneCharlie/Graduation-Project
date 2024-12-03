<template>
    <el-card class="box-card">
      <el-form :model="queryParams" ref="queryForm" v-show="showSearch" :inline="true">
        <el-form-item label="参与者用户名" prop="workItemUserName">
          <el-input
            v-model="queryParams.workItemUserName"
            placeholder="请输入参与者用户名"
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
              @click="selectInstNodeUser"
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
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>
  
      <el-table size="medium" style="width: 100%;margin-top: 20px;" :data="actUserList" v-loading="loading"
                @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="50" align="center" />
            <el-table-column type="index" label="序号" align="center" width="50"></el-table-column>
            <el-table-column prop="giveUserName" label="提交人姓名" align="center" width="130"></el-table-column>
            <el-table-column prop="actInsName" label="工作项" align="center"  width="150"></el-table-column>
            <el-table-column prop="workItemUserName"  label="参与者用户名	" align="center" width="250"></el-table-column>
            <el-table-column prop="orgName"  label="参与者组织	" align="center" width="250"></el-table-column>
            <el-table-column prop="procInstId" label="流程实例id" align="center" width="300"></el-table-column>
            <el-table-column prop="enStatus" label="状态" align="center" width="100"></el-table-column>
      </el-table>
  
      <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
  
      <!-- 添加或修改角色配置对话框 -->
      <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body>
        <el-transfer
          filterable
          :titles="['人员列表', '本次选中人员']"
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
      name: "actMgt",
      data() {
        return {
          actInsId: undefined,
          procInstId: undefined,

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
            page: 1,
            limit: 10,
            workItemUserName:'',
            actInsId:'',
            procInstId:'',
          },
          // 显示搜索条件
          showSearch: true,
          // 弹出层标题
          title: "",
          // 是否显示弹出层
          open: false,
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
            actInsId:'',
            procInstId:'',
            userIds: '',
          },
        }
      },
      methods: {
        getList() {
          this.loading = true;
          flowInstNodeActiveUserList(this.queryParams).then(response => {
              this.actUserList = response.data.records;
              this.total = parseInt(response.total);
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
          var userIds = '';
          for(var i = 0; i < this.hasSelectData.length; i++) {
            userIds += this.hasSelectData[i] + ',';
          }
        //   console.log(userIds);
          this.submitData.actInsId = this.actInsId
          this.submitData.procInstId = this.procInstId
          this.submitData.userIds = userIds.substring(0, userIds.length - 1)
          saveWorkItem(this.submitData).then(response => {
            if (response.code == 200) {
                this.$message.success(response.message);
            }else{
                this.$message.error(response.message);
            }
            this.open = false;
            this.getList();
          })

          // 添加之后 要刷新 用户信息
          this.flushSelectData();
          this.cancel();
        },
        /** 新增按钮操作 */
        handleAdd() {
          this.reset()
          this.generateData()
        },
        /** 删除按钮操作 */
        handleDelete(row) {
          const enIds = this.ids.join(',');
          console.log(enIds);
          this.$confirm('是否确认删除该数据?', "警告", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning"
            }).then(function() {
               return removeFlowInstNodeUser({
                    delids:enIds
               });
            }).then(() => {
              this.getList();
              this.msgSuccess("删除成功");
            })
            // 删除之后 要刷新 用户信息
            this.flushSelectData();
            this.cancel();
        },
/** 修改按钮操作 */

        // 表单重置
        reset() {
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
        // 刷新用户信息
        flushSelectData(){
          this.selectData = [];
        },
        /*
            获取用户列表信息
        */
       getUserMessageInfo(){
          var appendUserIds = '';
          this.actUserList.forEach((value, index, array) => {
            // console.log(this.actUserList[index].userId);
            appendUserIds += this.actUserList[index].userId + ',';
          })
          // console.log(appendUserIds);
        if ( this.selectData == null || this.selectData.length == 0) {
            selectActiveUser().then((response) => {
                this.lineList = response.data;
                this.lineList.forEach((value, index, array) => {
                    let bean = this.lineList[index];
                    var beanUserName = bean.userName;
                    // console.log(beanUserName + "--->||" + (appendUserIds.search(beanUserName)) );
                    this.selectData.push({
                        label:bean.nickName + ' ----- ' + bean.deptName,
                        key:bean.userId,
                        disabled: (appendUserIds.indexOf(beanUserName) != -1)
                    });
                })
            })
          }
       },
        /*
            关联人员 
        */
        selectInstNodeUser() {
          this.getUserMessageInfo();
          this.cancel();
          this.title = "请选择人员";
          this.open = true;
        },
      },
      mounted: function() {
        this.actInsId = this.$route.params.actInsId
        this.procInstId = this.$route.params.procInstId
        this.queryParams.actInsId = this.$route.params.actInsId
        this.queryParams.procInstId = this.$route.params.procInstId
        // console.log(this.actInsId)
        // console.log(this.procInstId)
        // console.log(this.queryParams.actInsId)
        // console.log(this.queryParams.procInstId)
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
  