<template>
  <div>
    <el-card style="height: auto;" class="marginTopCard box-card" shadow="never">
      <div slot="header" class="clearfix item">
        <span><i class="el-icon-s-tools" style="margin-right: 20px;"><b>设备列表</b></i></span>
      </div>
      <el-row>
        <el-col :span="24">
          <el-table :data="deviceList" border max-height="275" style="width: 100%">
            <el-table-column align="center" type="index" label="序号" width="50" fixed></el-table-column>
            <el-table-column align="center" prop="deviceNumber" label="设备编号" width="250" fixed></el-table-column>
            <el-table-column align="center" prop="deviceName" label="设备名称" width="250"></el-table-column>
            <el-table-column align="center" prop="useUnit" label="使用单位" width="250"></el-table-column>
            <el-table-column align="center" label="是否开启检验申请" width="140">
              <template slot-scope="scope">
                <el-tag v-if="scope.row.isRead == '1'" type="danger">未开启</el-tag>
                <el-tag v-if="scope.row.isRead == '0'" type="success">已开启</el-tag>
              </template>
            </el-table-column>
            <el-table-column align="center" label="是否领用" width="80">
              <template slot-scope="scope">
                <el-tag v-if="scope.row.isReceiving == '1'" type="danger">未领用</el-tag>
                <el-tag v-if="scope.row.isReceiving == '0'" type="success">已领用</el-tag>
              </template>
            </el-table-column>
            <el-table-column align="center" prop="receivingUserName" label="领用人" width="80"></el-table-column>
            <el-table-column align="center" prop="updateTime" label="领用日期" width="170"></el-table-column>
            <el-table-column align="center" label="任务状态" fixed="right">
              <template slot-scope="scope">
                <el-tag v-if="scope.row.isHandle == '1'" type="danger">未处理</el-tag>
                <el-tag v-if="scope.row.isHandle == '0'" type="success">已处理</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>

    </el-card>
  </div>
</template>

<script>
  import { getList } from '@/api/business/contractDevice'

  export default {
    name: 'innerDevice',
    data() {
      return {
        contractRowId: '',
        deviceList: [],
      }
    },
    methods: {
      getList() {
        if(this.contractRowId != null && this.contractRowId != '') {
          getList(this.contractRowId).then(response => {
            if(response.code === 200) {
              this.deviceList = response.data;
            }
          })
        }
      }
    },
    mounted() {
      this.contractRowId = this.$route.params.contractRowId;
      this.getList();
    },
  }


</script>

<style>
</style>
