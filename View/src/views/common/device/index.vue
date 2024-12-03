<template>
  <div>
    <el-card style="height: auto;" class="marginTopCard box-card" shadow="never">
      <div slot="header" class="clearfix item">
        <span><i class="el-icon-s-tools" style="margin-right: 20px;"><b>设备列表</b></i></span>
      </div>
      <el-row>
        <el-col :span="24">
          <el-table :data="deviceList" border max-height="275" style="width: 100%">
            <el-table-column align="center" type="index" label="序号" width="50" ></el-table-column>
            <el-table-column align="center" prop="deviceNumber" label="报告编号" width="250"></el-table-column>
            <el-table-column align="center" prop="deviceName" label="设备类别" width="350"></el-table-column>
            <el-table-column align="center" prop="useUnit" label="使用单位" ></el-table-column>
            <el-table-column align="center" prop="receivingUserName" label="任务领用人" ></el-table-column>
            <el-table-column align="center" prop="updateTime" label="领用日期" ></el-table-column>
            <el-table-column align="center" label="任务状态" width="100">
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
  import { saveDeviceList} from '@/api/business/contractDevice'

  export default {
    name: "deviceListPage",
    data() {
      return {
        isSelect: false,
      }
    },
    methods: {
      saveDeviceList() {
        if(this.deviceList != null && this.deviceList.length != 0) {
          saveDeviceList(this.deviceList).then(response => {
            if(response.code === 200) {
              console.log("device save success");
            } else {
              console.log("device is no save");
            }
          })
        }
      },
    },
    created() {

    },
    mounted: function() {

    },
    props: ['deviceList'],
  }
</script>

<style>
</style>
