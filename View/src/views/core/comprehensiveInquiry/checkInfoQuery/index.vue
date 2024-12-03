<template>
    <div class="app-container">
        <el-card class="box-card" shadow="never">
            <div class="text item">
              <i class="el-icon-document-copy" style="margin-right: 10px;"></i>
              检验信息查询
            </div>
        </el-card>
        <el-card style="margin-top: 8px;" shadow="never">
            <el-form ref="formItem" :model="queryParam" :inline="true">
              <el-form-item label="委托单位">

              </el-form-item>
            </el-form>
        </el-card>
    </div>
</template>

<script>
export default {
 data() {
  return {
    // 遮罩层
    loading: true,
    // 其他查询条件是否隐藏 ， 默认隐藏
    queryOtherCondition:false,
    // 查询参数
    queryParam: {
        pageNum:1,
        pageSize:10,

    },



  };
 },
 methods: {
     
 },


}
</script>

<style scoped>

</style>
