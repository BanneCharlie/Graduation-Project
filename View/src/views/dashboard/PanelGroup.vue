<template>
  <el-row :gutter="40" class="panel-group">
    <el-col :xs="12" :sm="12" :lg="4" class="card-panel-col">
      <div class="card-panel" @click="routeLinkByName('requestContract')">
        <div class="card-panel-icon-wrapper">
          <el-image
          style="width: 50px; height: 40px"
          :src="require('../../assets/homeImg/img_dbsy.png')">
        </el-image>
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            合同评审申请
          </div>
        </div>
      </div>
    </el-col>
    <el-col :xs="12" :sm="12" :lg="4" class="card-panel-col">
      <div class="card-panel" @click="routeLinkByName('myReport')">
        <div class="card-panel-icon-wrapper">
          <el-image
          style="width: 50px; height: 40px"
          :src="require('../../assets/homeImg/img_jymb.png')">
        </el-image>
        </div>
        <div class="card-panel-description">
            <div class="card-panel-text">
              我的报告
            </div>
        </div>
      </div>

<!--      <div v-else class="card-panel" @click="routeLinkByName('requestReport')">
        <div class="card-panel-icon-wrapper">
          <el-image
          style="width: 50px; height: 40px"
          :src="require('../../assets/homeImg/img_jymb.png')">
        </el-image>
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            检验报告申请
          </div>
        </div>
      </div>-->
    </el-col>
    <el-col :xs="12" :sm="12" :lg="4" class="card-panel-col">
      <div class="card-panel" @click="routeLinkByName('taskReceive')">
        <div class="card-panel-icon-wrapper">
          <el-image
          style="width: 50px; height: 40px"
          :src="require('../../assets/homeImg/img_ywgl.png')">
        </el-image>
        </div>
        <div class="card-panel-description">
          <el-badge :value="receiveCount" :max="99">
            <div class="card-panel-text"  :style="{'font-size' : receiveCount > 10 ? '16px' : '18px'}">
              待领取任务
            </div>
          </el-badge>
        </div>
      </div>
    </el-col>
    <!-- <el-col :xs="12" :sm="12" :lg="4" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper">
          <el-image
          style="width: 50px; height: 40px"
          :src="require('../../assets/homeImg/img_zhcx.png')">
        </el-image>
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            综合查询
          </div>
        </div>
      </div>
    </el-col> -->
    <!-- <el-col :xs="12" :sm="12" :lg="4" class="card-panel-col">
      <div class="card-panel" @click="routeLinkByName('productCheckCenter')">
        <div class="card-panel-icon-wrapper">
          <el-image
          style="width: 50px; height: 40px"
          :src="require('../../assets/homeImg/img_cpjy.png')">
        </el-image>
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">
            产品中心检验
          </div>
        </div>
      </div>
    </el-col> -->
  </el-row>
</template>

<script>
  import { getTaskReceiveCount , getWaitSendTaskCount } from '@/api/business/deviceTask'
  import { getCurrentUserDetailInfo } from '@/api/system/user'

export default {
  name: 'panelGroup',
  data() {
    return{
      receiveCount: 0,
      user:{},
      isSendTaskManage: false,
      waitSendTaskCount: 0,
    }
  },
  methods: {
    // 首页的 四个跳转按钮
    routeLinkByName(name){
      this.$emit('routeLinkByName',name);
    },
    getTaskReceiveCount() {
      getTaskReceiveCount().then(response => {
        if(response.code == 200) {
          this.receiveCount = response.data
        }
      })
    },
    getWaitSendTaskCountMethod(){
      getWaitSendTaskCount().then((response) => {
        this.waitSendTaskCount = parseInt(response.data);
      })
    },
    // 获取当前用户的登录信息
    getCurrentLoginUserInfo(){
      getCurrentUserDetailInfo().then((response) => {
        this.user = response.data;
        let roleList = this.user.roles;
        roleList.forEach((value, index, array) => {
          if(value.roleId == '131'){
            this.getWaitSendTaskCountMethod();
            this.isSendTaskManage = true;
            return;
          }
        });

        if(this.user.deptId == '10113' || this.user.userName == 'admin'){
          this.$emit('getUserFlag');
        }

      })
    }
  },
  mounted(){
    this.getCurrentLoginUserInfo();
    this.getTaskReceiveCount();
  }
}
</script>

<style lang="scss" scoped>
.panel-group {
  margin-top: 10px;

  .card-panel-col {
    margin-bottom: 20px;
  }

  .card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);

    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }

      .icon-people {
        background: #40c9c6;
      }

      .icon-message {
        background: #36a3f7;
      }

      .icon-money {
        background: #f4516c;
      }

      .icon-shopping {
        background: #34bfa3
      }
    }

    .icon-people {
      color: #40c9c6;
    }

    .icon-message {
      color: #36a3f7;
    }

    .icon-money {
      color: #f4516c;
    }

    .icon-shopping {
      color: #34bfa3
    }

    .card-panel-icon-wrapper {
      float: left;
      margin: 6px 0 0 3px;
      padding-top: 25px;
      padding-left: 10px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }

    .card-panel-icon {
      float: left;
      font-size: 48px;
    }

    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px;
      margin-left: 0px;

      .card-panel-text {
        line-height: 49px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 18px;
        margin-bottom: 12px;
      }

      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}
.card-panel-col{
  width: 250px;
}

@media (max-width:550px) {
  .card-panel-description {
    display: none;
  }

  .card-panel-icon-wrapper {
    float: none !important;
    width: 100%;
    height: 100%;
    margin: 0 !important;

    .svg-icon {
      display: block;
      margin: 14px auto !important;
      float: none !important;
    }
  }
}
</style>
