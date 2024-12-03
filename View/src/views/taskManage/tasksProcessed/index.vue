<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" v-show="showSearch" :inline="true">
      <el-form-item label="检验申请名称" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入申请名称"
          clearable
          size="small"
          style="width: 180px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="报告编号" prop="reportNumber">
        <el-input
          v-model="queryParams.reportNumber"
          placeholder="请输入报告编号"
          clearable
          size="small"
          style="width: 180px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备编号" prop="deviceNumber">
        <el-input
          v-model="queryParams.deviceNumber"
          placeholder="请输入设备编号"
          clearable
          size="small"
          style="width: 180px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="报告状态" prop="isHandle">
        <el-select
          v-model="queryParams.isHandle"
          placeholder="报告状态"
          clearable
          size="small"
          style="width: 100px"
        >
          <el-option
            v-for="status in handleStatus"
            :key="status.statusValue"
            :label="status.statusLabel"
            :value="status.statusValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-table style="width: 100%;" :data="taskProcessedList" v-loading="tableLoading">
      <el-table-column align="center" prop="deviceNumber" label="设备编号" width="240"></el-table-column>
      <el-table-column align="center" prop="deviceName" label="设备名称" width="200"></el-table-column>
      <el-table-column align="center" prop="reportNumber" label="报告编号" width="240"></el-table-column>
      <el-table-column align="center" prop="title" label="检验申请名称" width="250"></el-table-column>
      <el-table-column align="center" prop="isHandle" label="是否处理报告" width="100">
        <template slot-scope="scope">
            <el-tag v-if="scope.row.isHandle == '1'" type="danger">未处理</el-tag>
            <el-tag v-if="scope.row.isHandle == '0'" type="success">已处理</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="createTime" label="申请时间" width="150"></el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
            <el-button class="handleBtn" type="primary" icon="el-icon-edit-outline"
                       size="small" @click="handleDataView(scope.row)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>

</template>

<script>
  import { getTasksProcessed } from "@/api/business/deviceTask";
  import { goProcessTemplate } from "@/api/process/process";

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
          isRead: '0',
          isReceiving: '0',
          isHandle: '',
          title: '',
          reportNumber: '',
          deviceNumber: '',
        },
        total: 0,
        taskProcessedList: [],
        handleStatus: [
          {statusValue: '1', statusLabel: '未处理'},
          {statusValue: '0', statusLabel: '已处理'},
        ]
      }
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
        this.resetForm("queryForm");
        this.handleQuery();
      },
      //查询所有当前用户的待处理任务（任务管理菜单下）
      getList() {
        getTasksProcessed(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.total = response.total;
          this.taskProcessedList = [];
          this.taskProcessedList = this.dataConvert(response.rows);
        })
      },
      //时间格式转换，将时间字符串中带有T的转换为yyyy-MM-dd HH:mm:ss格式的时间字符
      dataConvert(list) {
        var retList = [];
        for(var i = 0; i < list.length; i++) {
          var bean = list[i]
          bean['createTime'] = bean['createTime'].replaceAll("T", " ").split(".")[0];
          retList.push(bean)
        }
        return retList;
      },
      handleDataView(row) {
        goProcessTemplate({
          fileRelationBeanId:  '',
          flag: '',
          swId: '',
          swtaskId: '',
          swtofw: '',
          type: '',
          processKey: '',
          businessKey:  row.businessKey,
          sxpzId: '',
          dwOrgId:  '',
          templateType:  'view',
          processInstId: row.piid
        }).then(response => {
          this.$router.push({name:'templatePageView', params:{processUrlVoJson: JSON.stringify(response.data)}});
        });
      }
    },
    mounted() {
      this.getList();
    }
  }
</script>

<style>
</style>
