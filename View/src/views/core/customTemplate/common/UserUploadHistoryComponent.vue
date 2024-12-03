<template>
	<div>
		<el-dialog
			title="历史附件信息"
			width="90%"
			center
			:visible.sync="showDialog"
			:show-close="false"
			:close-on-press-escape="false"
			:close-on-click-modal="false">
			<el-form ref="queryForm" size="mini" label-suffix=":" :inline="true" :model="queryParams">
				<el-form-item label="文件名称" prop="fileName">
					<el-input v-model="queryParams.fileName" placeholder="请输入文件名称" />
				</el-form-item>
				<el-form-item>
					<el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
					<el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
				</el-form-item>
			</el-form>
			<el-row :gutter="10" class="mb8">
				<el-col :span="1.5">
					<el-button size="mini" type="primary" icon="el-icon-circle-plus-outline" @click="handleAddFile">添加文件</el-button>
				</el-col>
			</el-row>

			<el-table v-loading="attachLoading" :data="userHistoryDataList" size="small">
				<el-table-column label="序号" type="index" align="center" width="80"></el-table-column>
				<el-table-column label="文件名称" align="center" prop="uploadName"></el-table-column>
				<el-table-column label="预览效果" align="center" width="200">
					<template slot-scope="scope">
						<el-image v-if="canDirectPreview(scope.row.fileType)" :src="viewImageFile(scope.row)" :preview-src-list="showImageGroupUrls"></el-image>
						<span v-else>暂不支持非图片文件</span>
					</template>
				</el-table-column>
				<el-table-column label="上传时间" align="center" prop="createTime" width="180"></el-table-column>
				<el-table-column label="操作" width="200" align="center" fixed="right">
					<template slot-scope="scope">
						<el-button type="primary" size="mini" plain @click="clickSelectData(scope.row)">选中数据</el-button>
						<el-button type="danger" size="mini" plain @click="clickRemoveSelfAttach(scope.row)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
			<pagination
				v-show="historyFileTotal > 0"
				:total="historyFileTotal"
				:page.sync="queryParams.pageNum"
				:limit.sync="queryParams.pageSize"
				@pagination="handleQuery" />

			<span slot="footer" class="dialog-footer">
				<el-button size="mini" plain type="danger" @click="clickCloseComponent">关 闭</el-button>
			</span>
		</el-dialog>

		<!-- 上传文件dialog相关 -->
		<el-dialog
			title="上传附件"
			width="50%"
			center
			:visible.sync="showUploadAttachDialog"
			:show-close="false"
			:close-on-press-escape="false"
			:close-on-click-modal="false">
			<el-row class="mb8" :gutter="20">
				<el-col :span="24">
					<el-upload
						class="upload-demo"
						ref="uploadAttach"
						action
						:on-change="changeFileList"
						:file-list="attachFileList"
						:show-file-list="false"
						:auto-upload="false">
						<el-button v-if="!attachLoading" icon="el-icon-search" slot="trigger" size="mini" type="info">选取文件</el-button>
					</el-upload>
					<el-table size="small" :data="attachFileList" border>
						<el-table-column label="序号" type="index" width="70" align="center"></el-table-column>
						<el-table-column label="文件名称" prop="name" align="center"></el-table-column>
						<el-table-column label="操作" align="center" width="150">
							<template slot-scope="scope">
								<el-button type="danger" size="mini" icon="el-icon-delete" @click="clikcRemoveFile(scope.row, scope.$index)">移除附件</el-button>
							</template>
						</el-table-column>
					</el-table>
				</el-col>
			</el-row>

			<span slot="footer" class="dialog-footer">
				<el-button size="mini" plain type="danger" @click="closeUploadAttachDialog">关 闭</el-button>
				<el-button v-if="!attachLoading" size="mini" plain type="success" @click="handleUploadAttachFileMethod">上传附件</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
import ruoyi from '@/utils/ruoyi';

import { getSelfAttachData, uploadAttach, removeSelfAttachData } from '@/api/customTemplate/reportAttach';

export default {
	components: {},
	props: {},
	data() {
		return {
			showDialog: true,

			queryParams: {
				pageNum: 1,
				pageSize: 10,
				fileName: null,
				dataType: 1
			},

			historyFileTotal: 0,
			userHistoryDataList: [],

			// 上传附件相关字段
			showUploadAttachDialog: false,
			attachLoading: false,
			attachFileList: [],
			// 大图预览图片地址列表
			showImageGroupUrls: []
			// ===== field end ======
		};
	},
	created() {},
	watch: {},
	mounted() {
		this.handleQuery();
	},
	methods: {
		// 点击处理查询
		handleQuery() {
			this.getUserHistoryAttachDataList();
		},
		// 获取用户历史附件数据列表
		getUserHistoryAttachDataList() {
			this.attachLoading = true;
			getSelfAttachData(this.queryParams).then(response => {
				this.historyFileTotal = response.total;
				this.userHistoryDataList = response.rows;

				this.userHistoryDataList.forEach(value => {
					if (value.fileType == 'jpg' || value.fileType == 'png') {
						let imageUrl = this.viewImageFile(value);
						this.showImageGroupUrls.push(imageUrl);
					}
				});
				setTimeout(() => {
					this.attachLoading = false;
				}, 500);
			});
		},
		// 重置查询
		resetQuery() {
			this.queryParams = {
				pageNum: 1,
				pageSize: 10,
				fileName: null,
				dataType: 1
			};
			this.handleQuery();
		},

		// 添加新的文件
		handleAddFile() {
			this.showUploadAttachDialog = true;
		},

		clickCloseComponent() {
			this.$emit('closeComponent');
		},

		// =============================
		// ====== 上传附件相关方法 ======
		// =============================
		changeFileList(file, fileList) {
			this.attachFileList = fileList;
		},
		// 移除附件
		clikcRemoveFile(row, removeIndex) {
			this.$confirm('您确定要删除此附件吗?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning',
				center: true
			})
				.then(() => {
					let rowId = row.rowId;
					// 本次选中的文件
					if (!rowId) {
						this.attachFileList.splice(removeIndex, 1);
					}
				})
				.catch(() => {});
		},
		// 执行上传附件文件具体方法
		handleUploadAttachFileMethod() {
			if (this.attachFileList.length != 0) {
				this.attachLoading = true;
				let formData = new FormData();
				// 从浏览器封装的File对象获取具体的文件对象列表
				let uploadAttachFileList = this.attachFileList.map(value => {
					return value.raw;
				});
				formData.append('dataType', '1');
				uploadAttachFileList.forEach(fileItem => {
					formData.append('attachFiles', fileItem);
				});
				uploadAttach(formData)
					.then(response => {
						this.$refs.uploadAttach.clearFiles();
						this.attachFileList = [];
						this.attachLoading = false;
						this.closeUploadAttachDialog();
					})
					.catch(value => {
						this.attachLoading = false;
						this.closeUploadAttachDialog();
					});
			} else {
				this.$message.error('您还未选中附件信息!');
			}
		},
		//能否直接预览
		canDirectPreview(fileType) {
			return fileType && (fileType.toLowerCase() == 'jpg' || fileType.toLowerCase() == 'png');
		},
		// 预览图片信息
		viewImageFile(row) {
			let resultAttachImagePath = ruoyi.serverUrl + '/api/attach/preview-attach/' + row.rowId;
			return resultAttachImagePath;
		},
		// 点击选中当前附件数据信息
		clickSelectData(row) {
			let fileType = row.fileType;
			let insertHtml = null;
			if (fileType.toLowerCase() == 'jpg' || fileType.toLowerCase() == 'png') {
				insertHtml = '<img src="' + this.viewImageFile(row) + '"  style="width:90%;" title="图表文件" />';
			}
			this.$emit('selectAttach', insertHtml);
		},
		// 点击移除当前选中的附件信息
		clickRemoveSelfAttach(row) {
			this.$confirm('您确定要移除当前上传的附件信息吗(不可恢复!)?', '提示', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning',
				center: true
			})
				.then(() => {
					let rowId = row.rowId;
					removeSelfAttachData(rowId).then(response => {
						this.$message.success('删除成功!');
						this.handleQuery();
					});
				})
				.catch(() => {});
		},
		closeUploadAttachDialog() {
			this.showUploadAttachDialog = false;
			this.handleQuery();
		}

		// ====== methods end ======
	}
};
</script>

<style></style>
