<template>
	<div class="app-container">
		<el-form :model="queryParams" ref="queryForm" size="mini" v-show="showSearch" :inline="true">
			<el-form-item label="合同编号" prop="contractNumber">
				<el-input v-model="queryParams.contractNumber" placeholder="请输入合同编号" style="width: 240px" @keyup.enter.native="handleQuery" />
			</el-form-item>
			<el-form-item label="领取人" prop="receivingUserName">
				<el-input v-model="queryParams.receivingUserName" placeholder="请输入领取人" style="width: 240px" @keyup.enter.native="handleQuery" />
			</el-form-item>
			<!-- <el-form-item label="模板内容" prop="templateContent">
        <el-input v-model="queryParams.templateContent" placeholder="请输入数据源名称" clearable size="small" style="width: 240px"
          @keyup.enter.native="handleQuery" />
      </el-form-item> -->
			<el-form-item label="模板类别" prop="templateCategory">
				<!-- :disabled="deviceTypeDisabled"  -->
				<el-select v-model="queryParams.templateCategory" placeholder="请选择">
					<el-option v-for="dict in deviceType" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue"></el-option>
				</el-select>
			</el-form-item>
			<el-form-item>
				<el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
				<el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
			</el-form-item>
		</el-form>

		<el-table :data="data" size="small" v-loading="loading">
			<el-table-column type="index" label="序号" align="center" width="80"></el-table-column>
			<el-table-column prop="templateName" label="模板名称" align="center" width="300"></el-table-column>
			<el-table-column prop="templateCategory" label="模板种类" align="center" width="80"></el-table-column>
			<el-table-column prop="contractNumber" label="合同编号" align="center" width="200"></el-table-column>
			<el-table-column prop="deviceNumber" label="报告编号" align="center" width="200"></el-table-column>
			<el-table-column prop="receivingUserName" label="领取人" align="center" width="100"></el-table-column>
			<el-table-column prop="createTime" label="报告生成时间" align="center" width="150"></el-table-column>
			<el-table-column prop="isPrint" label="报告状态" align="center" width="90" fixed="right">
				<template slot-scope="scope">
					<el-tag v-if="scope.row.isStartFlow != '1'" type="danger" size="mini">未审核</el-tag>
					<el-tag v-else-if="scope.row.isReset == '1'" type="warning" size="mini">申请人修改</el-tag>
					<el-tag v-else-if="scope.row.isPrint != '1'" type="warning" size="mini">审核中</el-tag>
					<el-tag v-else type="success" size="mini">审核通过</el-tag>
				</template>
			</el-table-column>
			<el-table-column label="操作" align="center" fixed="right" width="230">
				<template slot-scope="scope">

<!--          <el-button
						v-if="scope.row.isPrint == '1'"
						size="mini"
						class="handleBtn"
						icon="el-icon-printer"
						@click="handleClickPrintReport(scope.row)"
						type="text">
						打印附件
					</el-button>

          <el-button
            v-if="scope.row.isPrint == '1'"
            size="mini"
            class="handleBtn"
            icon="el-icon-printer"
            @click="handlePrintReport(scope.row)"
            type="text">
            打印报告
          </el-button>-->
          <span v-if="scope.row.isStartFlow == '1' && scope.row.isReset != '1'">

           <el-button v-if="scope.row.isStartFlow == '1'" size="mini" type="text" icon="el-icon-s-promotion" @click="viewProcessStep(scope.row)">
              查看进度
            </el-button>

            <el-dropdown @command="handlePrintClick($event, scope.row)" trigger="click">
							<el-button icon="el-icon-arrow-down el-icon--right" size="mini" class="handleBtn" type="text">打印</el-button>
							<el-dropdown-menu slot="dropdown" :split-button="true">
								 <el-dropdown-item icon="el-icon-s-order" command="print_report">打印报告</el-dropdown-item>
								<el-dropdown-item icon="el-icon-s-tools" command="print_pdf_report">打印pdf附件</el-dropdown-item>
							</el-dropdown-menu>
						</el-dropdown>
          </span>

					<span v-else-if="scope.row.isStartFlow != '1' || scope.row.isReset == '1'">
						<el-button v-if="scope.row.isStartFlow != '1'" size="mini" icon="el-icon-thumb" type="text" @click="startExamineThisReport(scope.row)">
							审核报告
						</el-button>

						<el-dropdown @command="handleClick($event, scope.row)" trigger="click">
							<el-button icon="el-icon-arrow-down el-icon--right" size="mini" class="handleBtn" type="text">更多</el-button>
							<el-dropdown-menu slot="dropdown" :split-button="true">
								 <el-dropdown-item icon="el-icon-s-order" command="click_preview_report">预览报告</el-dropdown-item>
								<el-dropdown-item icon="el-icon-s-tools" command="edit_generic_content">配置报告内容</el-dropdown-item>
								<el-dropdown-item icon="el-icon-upload" command="edit_generic_upload">导入pdf</el-dropdown-item>
							</el-dropdown-menu>
						</el-dropdown>
					</span>

				</template>
			</el-table-column>
		</el-table>

		
    <!-- 用户导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
		<el-upload
		  ref="upload"
		  :limit="1"
		  accept=".pdf"
		  :headers="upload.headers"
		  :action="upload.url"
		  :data="uploadParams"
		  :disabled="upload.isUploading"
		  :on-progress="handleFileUploadProgress"
		  :on-success="handleFileSuccess"
		  :auto-upload="false"
		  drag
		>
		  <i class="el-icon-upload"></i>
		  <div class="el-upload__text">
			将文件拖到此处，或
			<em>点击上传</em>
		  </div>
		  <div class="el-upload__tip" style="color:red" slot="tip">提示：仅允许导入“pdf”格式文件！</div>
		  <div class="el-upload__tip" style="color:red" slot="tip">文件上传后，会自动更新当前报告。</div>
		  <div class="el-upload__tip" style="color:red" slot="tip">只保留模板页和上传文件，谨慎上传！</div>
		</el-upload>
		<div slot="footer" class="dialog-footer">
		  <el-button type="primary" @click="submitFileForm">确 定</el-button>
		  <el-button @click="upload.open = false">取 消</el-button>
		</div>
	  </el-dialog>

		<pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getReportList" />
	</div>
</template>

<script>
import { getMineReportList } from '@/api/business/deviceTask';
import { getFormInfoByReportId } from '@/api/business/reportExamine';
import { getProcessList, goProcessTemplate } from '@/api/process/process';
import { getToken } from "@/utils/auth";

const SXID = 'key-report-examine';

export default {
	name:'MyReport' ,
	// name: 'ReportTemplate',
	data() {
		return {
			// 总条数
			total: 0,
			loading: true,
			open: false,
			modalTitle: '',
			spinShow: false,
			isEdit: false,
			// 查询参数
			queryParams: {
				pageNum: 1,
				pageSize: 10,
				contractNumber: undefined,
				templateCategory: undefined,
				receivingUserName: undefined
			},
			data: [],
			// 设备种类 从 合同评审中获得值
			deviceType: [],
			// 显示搜索条件
			showSearch: true,
			// 非多个禁用
			multiple: true,

			// 报告审核流程业务配置项信息
			processSxpzInfo: {
				processKey: '',
				businessKey: '',
				templateType: '',
				sxpzId: '',
				swId: '', //
				type: '', //
				dwOrgId: '',
				flag: '', //
				fileRelationBeanId: '', //
				swtofw: '', //
				swtaskId: '' //
			},

			//报告导入参数
			upload: {
				// 是否显示弹出层（用户导入）
				open: false,
				// 弹出层标题（用户导入）
				title: "",
				// 是否禁用上传
				isUploading: false,
				// 是否更新已经存在的用户数据
				updateSupport: 0,
				// 设置上传的请求头部
				headers: { Authorization: "Bearer " + getToken() },
				// 上传的地址
				url: process.env.VUE_APP_BASE_API + "/report-template/importData"
			},
			
			uploadParams:{
				id:null
			}
		};
	},
	created() {
		this.getDeviceCateDict().then(response => {
			this.deviceType = response.data;
		});
	},
	mounted: function () {
		this.getProcessConfig();
		this.getReportList();
	},
	methods: {
		getReportList() {
			this.loading = true;
			getMineReportList(this.queryParams).then(response => {
				this.data = response.rows;
				this.total = response.total;
				this.loading = false;
			});
		},
		// 查看进度
		viewProcessStep(row) {
			getFormInfoByReportId(row.reportId).then(response => {
				let businessData;
				if (response.code === 200 && (businessData = response.data) != null) {
					goProcessTemplate({
						fileRelationBeanId: '',
						flag: '',
						swId: '',
						swtaskId: '',
						swtofw: '',
						type: '',
						processKey: '',
						businessKey: businessData.businessKey,
						sxpzId: '',
						dwOrgId: '',
						templateType: 'view',
						processInstId: businessData.piid
					}).then(response => {
						this.$router.push({
							name: 'templatePageView',
							params: { processUrlVoJson: JSON.stringify(response.data) }
						});
					});
				}
			});
		},
		/** 搜索按钮操作 */
		handleQuery() {
			this.queryParams.pageNum = 1;
			this.getReportList();
		},
		/** 重置按钮操作 */
		resetQuery() {
			this.resetForm('queryForm');
			this.handleQuery();
		},

		/*  流程相关  */
		/*
		 *  1. 开启报告审核流程
		 *
		 * */
		getProcessConfig() {
			getProcessList(SXID).then(response => {
				this.processSxpzInfo = response.data.sxpzList[0];
			});
		},
		startExamineThisReport(rowData) {
			goProcessTemplate({
				processKey: this.processSxpzInfo.ptId,
				businessKey: this.processSxpzInfo.sxId,
				sxpzId: this.processSxpzInfo.sxpzRowId,
				dwOrgId: this.processSxpzInfo.deptId,
				templateType: 'start',
				processInstId: '',
				reportType: rowData.templateCategory,
				reportId: rowData.reportId
			}).then(response => {
				this.$router.push({
					name: 'templatePage',
					params: { processUrlVoJson: JSON.stringify(response.data) }
				});
			});
		},

		// 点击跳转到预览报告页面
		handleClickPreviewReport(row) {
/* 			let param = '?reportId=' + row.reportId+ '&filePages=' + row.filePages ;
			let filePath = '/static/template/preview.html';
			let targerUrl = 'http://' + window.location.host + filePath + param;
			window.open(targerUrl);
      console.log("预览报告"); */

      this.$router.push("/templateView/" + row.reportId);
		},

		// 点击前往打印PDF页面
		handleClickPrintReport(row) {
			let param = '?reportId=' + row.reportId + '&contractReviewId=' + row.contractId;
			let filePath = '/static/template/printPDF.html';
			let targerUrl = 'http://' + window.location.host + filePath + param;

			if(row.fileName==null||row.fileName==undefined||row.fileName ==""){
				this.$confirm('该报告尚未上传附件，请先上传附件','提示',{
						confirmButtonText: "确定",
						cancelButtonText: "取消",
						type: "error"
						}).then(()=>{
							this.handleUploadReport(row.reportId);
						}).catch((value) => {
							this.$message.warning('您取消了本次上传文件');
						})
					}else{
						window.open(targerUrl);
					}
		},
			
    handlePrintReport(row){
      this.$router.push("/templateView/" + row.reportId);
		},


    handlePrintClick(name,row){
      switch (name) {
        case 'print_report':
           this.handlePrintReport(row);
           break;
        case 'print_pdf_report':
           this.handleClickPrintReport(row);
           break;
      }
    },


		// ------------------
		handleClick(name, row) {
			switch (name) {
				case 'edit_list_content':
					this.$router.push({
						name: 'reportTempletContent',
						params: {
							templateId: row.templateId,
							contractId: row.contractId,
							reportId: row.reportId
						}
					});
					break;
				case 'edit_generic_content':
					this.$router.push({
						name: 'reportTempletGenericContent',
						params: {
							templateId: row.templateId,
							reportId: row.reportId
						}
					});
					break;
				case 'click_preview_report':
					this.handleClickPreviewReport(row);
					break;
				case 'edit_generic_upload':
					if(row.fileName!=null&&row.fileName!=undefined&&row.fileName!=""){
						this.$confirm('已上传过报告文件pdf，确定覆盖于'+row.fileUploadTime+'上传的报告文件吗: ' + row.fileName+' 吗？','提示',{
						confirmButtonText: "确定",
						cancelButtonText: "取消",
						type: "error"
						}).then(()=>{
							this.handleUploadReport(row.reportId);
						}).catch((value) => {
							this.$message.warning('您取消了本次上传文件');
						})
					}else{
						this.handleUploadReport(row.reportId);
					}
					break;
			}
		},
		//报告上传
		handleUploadReport(reportId){
			this.upload.title = "报告内容导入";
			this.upload.open = true;		
			this.uploadParams.id = reportId;
		},
		// 文件上传中处理
		handleFileUploadProgress(event, file, fileList) {
			this.upload.isUploading = true;
		},
		// 文件上传成功处理
		handleFileSuccess(response, file, fileList) {
			this.upload.open = false;
			this.upload.isUploading = false;
			this.$refs.upload.clearFiles();
			this.$alert(response.msg, "导入结果", { dangerouslyUseHTMLString: true });
			this.getReportList();
		},
		// 提交上传文件
		submitFileForm() {
			this.$refs.upload.submit();
		}
	}
};
</script>
<style>
.searchItem {
	width: 300px;
	float: left;
}

.handleBtn {
	margin-left: 15px;
}

.el-dropdown-link {
	cursor: pointer;
	color: #409eff;
}
.el-icon-arrow-down {
	font-size: 12px;
}
.el-dropdown-menu--medium .el-dropdown-menu__item {
	font-size: 12px;
}
</style>
