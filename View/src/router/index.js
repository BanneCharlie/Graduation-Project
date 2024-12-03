import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

/* Layout */
import Layout from '@/layout';
import ParentView from '@/components/ParentView';

/**
 * Note: 路由配置项
 *
 * hidden: true                   // 当设置 true 的时候该路由不会再侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
 * alwaysShow: true               // 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
 *                                // 只有一个时，会将那个子路由当做根路由显示在侧边栏--如引导页面
 *                                // 若你想不管路由下面的 children 声明的个数都显示你的根路由
 *                                // 你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由
 * redirect: noRedirect           // 当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
 * name:'router-name'             // 设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题
 * meta : {
    noCache: true                // 如果设置为true，则不会被 <keep-alive> 缓存(默认 false)
    title: 'title'               // 设置该路由在侧边栏和面包屑中展示的名字
    icon: 'svg-name'             // 设置该路由的图标，对应路径src/assets/icons/svg
    breadcrumb: false            // 如果设置为false，则不会在breadcrumb面包屑中显示
  }
 */

// 公共路由
export const constantRoutes = [
	{
		path: '/redirect',
		component: Layout,
		hidden: true,
		children: [
			{
				path: '/redirect/:path(.*)',
				component: resolve => require(['@/views/redirect'], resolve)
			}
		]
	},
	{
		path: '/login',
		component: resolve => require(['@/views/login'], resolve),
		hidden: true
	},
	{
		path: '/template/phone',
		component: resolve => require(['@/views/phone'], resolve),
		hidden: true
	},
	{
		path: '/404',
		component: resolve => require(['@/views/error/404'], resolve),
		hidden: true
	},
	{
		path: '/401',
		component: resolve => require(['@/views/error/401'], resolve),
		hidden: true
	},
	{
		path: '',
		component: Layout,
		redirect: 'index',
		children: [
			{
				path: 'index',
				component: resolve => require(['@/views/index'], resolve),
				name: '首页',
				meta: { title: '首页', icon: 'dashboard', noCache: true, affix: true }
			}
		]
	},
	{
		path: '/user',
		component: Layout,
		hidden: true,
		redirect: 'noredirect',
		children: [
			{
				path: 'profile',
				component: resolve => require(['@/views/system/user/profile/index'], resolve),
				name: 'Profile',
				meta: { title: '个人中心', icon: 'user' }
			}
		]
	},
	{
		path: '/dict',
		component: Layout,
		hidden: true,
		children: [
			{
				path: 'type/data/:dictId(\\d+)',
				component: resolve => require(['@/views/system/dict/data'], resolve),
				name: 'Data',
				meta: { title: '字典数据', icon: '' }
			}
		]
	},
	{
		path: '/job',
		component: Layout,
		hidden: true,
		children: [
			{
				path: 'log',
				component: resolve => require(['@/views/monitor/job/log'], resolve),
				name: 'JobLog',
				meta: { title: '调度日志' }
			}
		]
	},
	{
		path: '/gen',
		component: Layout,
		hidden: true,
		children: [
			{
				path: 'edit/:tableId(\\d+)',
				component: resolve => require(['@/views/tool/gen/editTable'], resolve),
				name: 'GenEdit',
				meta: { title: '修改生成配置' }
			}
		]
	},
	// 检验模板 路由配置
	{
		path: '/customTemplate',
		name: 'customTemplate',
		component: Layout,
		hidden: true,
		meta: {
			notCache: true,
			hideInBread: true,
			hideInMenu: true
		},
		children: [
			{
				path: '/template/:dataSourceId/:templateId',
				name: 'reportTempletContent',
				meta: {
					icon: 'md-notifications',
					title: '配置报告检验列表'
				},
				component: () => import('@/views/core/customTemplate/template/index.vue')
			},
			{
				path: '/reportTempletGenericContent/:templateId/:reportId',
				name: 'reportTempletGenericContent',
				meta: {
					icon: 'md-notifications',
					title: '配置报告内容'
				},
				component: () => import('@/views/core/customTemplate/generic/index.vue')
			},
      {
        path: '/templateView/:reportId',
        name: 'reportTempletContentRealView',
        meta: {
          icon: 'md-notifications',
          title: '预览/打印报告内容'
        },
        component: () => import('@/views/core/customTemplate/view/index.vue')
      }
		]
	},
	// template
	{
		path: '/templatePage',
		name: 'templatePage',
		component: Layout,
		hidden: true,
		meta: {
			notCache: true,
			hideInBread: true,
			hideInMenu: true
		},
		children: [
			{
				path: '/templatePage/:processUrlVoJson',
				name: 'templatePage',
				meta: {
					icon: 'md-notifications',
					title: '启动流程'
				},
				component: () => import('@/views/common/template/index.vue')
			},
			{
				path: '/templatePageUpdate/:processUrlVoJson',
				name: 'templatePageUpdate',
				meta: {
					icon: 'md-notifications',
					title: '处理流程'
				},
				component: () => import('@/views/common/template/index.vue')
			},
			{
				path: '/templatePageView/:processUrlVoJson',
				name: 'templatePageView',
				meta: {
					icon: 'md-notifications',
					title: '查看申请'
				},
				component: () => import('@/views/common/template/index.vue')
			}
		]
	},
	// 编辑流程节点
	{
		path: '/procDefAct',
		name: 'procDefAct',
		component: Layout,
		hidden: true,
		meta: {
			notCache: true,
			hideInBread: true,
			hideInMenu: true
		},
		children: [
			{
				path: '/procDefAct/:procDefId',
				name: 'editProcDefAct',
				meta: {
					icon: 'md-notifications',
					title: '编辑流程节点'
				},
				component: () => import('@/views/workflow/procDefAct/index.vue')
			}
		]
	},
	// 编辑节点下一步
	{
		path: '/actDefNext',
		name: 'actDefNext',
		component: Layout,
		hidden: true,
		meta: {
			notCache: true,
			hideInBread: true,
			hideInMenu: true
		},
		children: [
			{
				path: '/actDefNext/:actDefId/:procDefId',
				name: 'editActDefNext',
				meta: {
					icon: 'md-notifications',
					title: '编辑节点下一步'
				},
				component: () => import('@/views/workflow/actDefNext/index.vue')
			}
		]
	},
	// 节点参与者
	{
		path: '/userlist',
		name: 'userlist',
		component: Layout,
		hidden: true,
		meta: {
			notCache: true,
			hideInBread: true,
			hideInMenu: true
		},
		children: [
			{
				path: '/userlist/:actDefId/:procDefId',
				name: 'userList',
				meta: {
					icon: 'md-notifications',
					title: '节点参与者'
				},
				component: () => import('@/views/workflow/userlist/index.vue')
			}
		]
	},

	// ------ 流程实例 路由
	// 编辑流程实例节点下一步
	{
		path: '/workFlowNode/node',
		name: 'instNode',
		component: Layout,
		hidden: true,
		meta: {
			notCache: true,
			hideInBread: true,
			hideInMenu: true
		},
		children: [
			{
				path: '/workFlowNode/node/:procInstId',
				name: 'instNode',
				meta: {
					icon: 'md-notifications',
					title: '编辑流程实例节点'
				},
				component: () => import('@/views/workflow/workFlowNode/node/index.vue')
			}
		]
	},

	// 节点参与者
	{
		path: '/workFlowNode/userList',
		name: 'instUser',
		component: Layout,
		hidden: true,
		meta: {
			notCache: true,
			hideInBread: true,
			hideInMenu: true
		},
		children: [
			{
				path: '/workFlowNode/userList/:procInstId/:actInsId',
				name: 'instUser',
				meta: {
					icon: 'md-notifications',
					title: '节点参与者'
				},
				component: () => import('@/views/workflow/workFlowNode/userList/index.vue')
			}
		]
	},

	// 综合查询->委托业务查询->查看设备列表
	{
		path: '/comStatistics/requestStatistics/innerDevice',
		name: 'innerDevice',
		component: Layout,
		hidden: true,
		meta: {
			notCache: true,
			hideInBread: true,
			hideInMenu: true
		},
		children: [
			{
				path: '/comStatistics/requestStatistics/innerDevice/:contractRowId/:contractRowId',
				name: 'innerDevice',
				meta: {
					icon: 'md-notifications',
					title: '查看设备列表'
				},
				component: () => import('@/views/comStatistics/requestStatistics/innerDevice/index.vue')
			}
		]
	}
];

export default new Router({
	mode: 'history', // 去掉url中的#
	scrollBehavior: () => ({ y: 0 }),
	routes: constantRoutes
});
