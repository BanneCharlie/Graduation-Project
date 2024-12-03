<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" v-show="showSearch" :inline="true">
      <el-form-item label="查询范围">
        <el-date-picker
          v-model="queryParams.startTime"
          type="datetime"
          format="yyyy-MM-dd HH:mm:ss"
          value-format="yyyy-MM-dd HH:mm:ss"
          placeholder="选择开始时间" size="small">
        </el-date-picker>
        <span style="margin-left: 8px;margin-right: 8px;">至</span>
        <el-date-picker
          v-model="queryParams.endTime"
          type="datetime" format="yyyy-MM-dd HH:mm:ss"
          value-format="yyyy-MM-dd HH:mm:ss"
          placeholder="选择结束时间" size="small">
        </el-date-picker>
      </el-form-item>


      <el-form-item label="申请部门" prop="checkDeptId">
        <el-select
          v-model="queryParams.checkDeptId"
          placeholder="申请部门"
          clearable
          size="small"
          style="width: 200px"
        >
        <el-option
          v-for="dept in deptList"
          :key="dept.deptId"
          :label="dept.deptName"
          :value="dept.deptId"
        />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="tableList"
              border style="width: 100%">
      <el-table-column fixed align="center" prop="contractNumber" label="合同编号" width="200" sortable></el-table-column>
      <el-table-column align="center" prop="reportNumber" label="报告编号" width="200"></el-table-column>
      <el-table-column align="center" prop="contractMoney" label="合同金额" width="120"></el-table-column>
      <el-table-column align="center" prop="checkDept" label="检验部门" width="120"></el-table-column>
      <el-table-column align="center" label="检验员" width="200">
        <template slot-scope="scope">
          <el-tooltip placement="top">
            <div slot="content">{{scope.row.checkUserNameMore}}</div>
            <div>{{scope.row.checkUserName}}</div>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="userCount" label="检验员数量" width="100"></el-table-column>
      <el-table-column align="center" prop="deviceCount" label="设备数量" width="100"></el-table-column>
      <el-table-column align="center" prop="createTime" label="创建时间" width="200"></el-table-column>
      <el-table-column fixed="right" align="center" label="操作" width="200">
        <template slot-scope="scope">
          <el-button @click="handleClick(scope.row)" type="primary" size="small">查看设备列表</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      :page-sizes="[10,20,50,100]"
      :page-size="10"
      @pagination="getList"
      style="margin-bottom: 20px;"
    />
  </div>
</template>

<script>
  import { getContractAndReportInfo } from "@/api/business/requestStatistics";
  import { listDept } from "@/api/system/dept";

   export default {
     data() {
       return {
         tableList: [],
         // 日期范围
         dateRange: [],
         // 显示搜索条件
         showSearch: true,
         queryParams: {
           pageNum: 1,
           pageSize: 10,
           checkDeptId: '',
           startTime: undefined,
           endTime: undefined,
         },
         total: 0,
         deptList: [],
       }
     },
      methods: {
        getList() {
          getContractAndReportInfo(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
            this.total = response.total;
            this.tableList = [];
            this.tableList = this.dataConvert(response.rows);
            for(var i = 0; i < this.tableList.length; i++) {
              var names = this.tableList[i].checkUserNameMore;
              var userCount = 0 ;
              if(names != null && names != '') {
                userCount = this.tableList[i].checkUserNameMore.split(",");
              }
              this.tableList['userCount'] = userCount;
            }
          })
        },
        getDeptList() {
          listDept().then(response => {
            if(response.code === 200) {
              this.deptList = response.data;
            }
          })
        },
        /** 搜索按钮操作 */
        handleQuery() {
          this.queryParams.pageNum = 1;
          this.getList();
        },
        /** 重置按钮操作 */
        resetQuery() {
          this.queryParams.startTime = undefined;
          this.queryParams.endTime = undefined;
          this.dateRange = [];
          this.resetForm("queryForm");
          this.handleQuery();
        },
        handleClick(row) {
          this.$router.push({name:'innerDevice', params:{contractRowId: row.reqContRowId}});
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
      },
      mounted() {
        this.getList();
        this.getDeptList();
      }
    }

</script>

<style>
</style>
