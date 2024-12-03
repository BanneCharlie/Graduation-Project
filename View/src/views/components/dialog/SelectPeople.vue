<template>
  <div>
    <treeTransfer :title="title" :from_data='leftData'
                  :to_data='toData' :defaultProps="{label:'label'}"
                  @add-btn='add' @remove-btn='remove' :mode='mode'
                  height='500px' filter :openAll='isExpandAll'>
    </treeTransfer>
  </div>
</template>

<script>
  import treeTransfer from 'el-tree-transfer' // 引入树形穿梭框
  import { deptUserTree } from "@/api/system/dept";

  export default {
    name: 'selectPeople',
    components: {
      treeTransfer
    },
    data() {
      return {
        title: ["待选人员", "已选人员"],
        mode: "transfer", // transfer addressList
        isExpandAll: false,
        leftData: [], //显示数据，需要对其中的数据进行操作
        toData: [],
        hasSelectedInfo: [],
        delSelectedInfo: []
      }
    },
    //---- methods
    methods: {
      getDeptUserTree() {
        deptUserTree().then(response => {
          if(response.code === 200) {
            var treeList = response.data;
            for(var i = 0; i < treeList.length; i++) {
              treeList[i].pid = 0
              this.leftData = treeList;
            }
          } else {
            this.$message({
              showClose: true,
              message: '获取用户信息失败，请联系管理员',
              type: error
            });
          }
        })
      },
      // 切换模式 现有树形穿梭框模式transfer 和通讯录模式addressList
      changeMode() {
        if (this.mode == "transfer") {
          this.mode = "addressList";
        } else {
          this.mode = "transfer";
        }
      },
      // 监听穿梭框组件添加
      add(leftData,toData,obj){
        this.hasSelectedInfo = [];
        this.recursionGetPeople(toData)
        this.hasSelectedInfo = JSON.parse(JSON.stringify(this.hasSelectedInfo))
      },
      // 监听穿梭框组件移除
      remove(leftData,toData,obj){
        var delArr = obj.nodes;
        for(var i = 0; i < delArr.length; i++) {
          var index = this.getIndexByUserId(delArr[i].id)
          if(index != -1) {
            this.hasSelectedInfo.splice(index, 1);
          }
        }
      },
      //递归的往 hasSelectedInfo 中添加已经选择的人员信息
      recursionGetPeople(tree) {
        // this.hasSelectedInfo = [];
        for(var i = 0; i < tree.length; i++) {
        var node = tree[i]
        if(node.children == null) {
          var bean = {userId: node.id, username: node.label};
          this.hasSelectedInfo.push(JSON.parse(JSON.stringify(bean)))
        } else {
          this.recursionGetPeople(node.children)
        }
        }
      },
      getIndexByUserId(userId) {
        for(var i = 0; i < this.hasSelectedInfo.length; i++) {
          var bean = this.hasSelectedInfo[i];
          if(bean.userId === userId) {
            return i;
          }
        }
        return -1;
      },
      getSelectPeopleByDialog() {
        return this.hasSelectedInfo;
      },
      resetToData() {
        this.toData = [];
        this.leftData = [];
        this.hasSelectedInfo = [];
        this.delSelectedInfo = [];
        this.getDeptUserTree();
      }
    },
    mounted() {
      this.resetToData();
      this.getDeptUserTree();
    },
    created() {
      this.resetToData();
    },
  }
</script>

<style>
</style>
