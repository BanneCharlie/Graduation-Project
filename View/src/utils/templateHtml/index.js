const INNER_TARGET_TAG_ARR = ['编制：', '审核：', '批准：'];
const HELP_LOCATE_CONTENT = '日期：';
const INNER_TARGET_TAG_GROUP_CLASS_NAME_ARR = ['compositionTypeBianZhi', 'compositionTypeShenHe', 'compositionTypePiZhun'];

/**
 * 寻找内部标签信息
 * @param {*} sourceContent 源内容
 * @param {*} locateContent 要搜索的内容
 * @returns 返回一个当前目标查找的HtmlNode对象节点
 */
function findInnerTag(sourceContent, locateContent) {
	let returnTag = {
		metaContentData: null, // 元内容数据信息
		tagName: null, // 标签名称
		tagBeginIndex: -1, // 标签起始位置
		tagEndIndex: -1, // 标签结束位置
		locateContent: null, // 搜索内容
		locateBeginIndex: -1, // 搜索内容起始索引
		locateEndIndex: -1 // 搜索内容结束索引
	};

	let locateFindIndex = sourceContent.indexOf(locateContent);
	if (locateFindIndex == -1) {
		return null;
	}

	// 闭合标签的临时索引值
	let closeTagBeginIndex = sourceContent.indexOf('<', locateFindIndex);
	let closeTagEndIndex = closeTagBeginIndex + 1;
	while (sourceContent.charAt(closeTagEndIndex) != '>') {
		closeTagEndIndex++;
	}
	// 需要定位到 > 位置索引再+1
	closeTagEndIndex++;
	// 获取到当前包裹到要搜索内容的标签内容 --> tagName
	let tagName = sourceContent.substring(closeTagBeginIndex + 2, closeTagEndIndex - 1);

	for (let index = closeTagBeginIndex - 1; index > 0; index--) {
		const prefixChart = sourceContent[index];
		// 找到当前左侧标签开始位置
		if (prefixChart == '<') {
			let startOffset = index + 1;
			let startEndOffset = startOffset + tagName.length;
			// 左侧的开始标签name
			let prefixTagName = sourceContent.substring(startOffset, startEndOffset);

			// console.log('startOffset==', startOffset);
			// console.log('startEndOffset==', startEndOffset);
			// console.log('prefixTagName==', prefixTagName);

			// 闭合成功
			if (prefixTagName == tagName) {
				// 判断是否有过class属性了
				let nowHtmlNode = sourceContent.substring(index, closeTagEndIndex);
				let existClassAttrubute = nowHtmlNode.indexOf('class=', startEndOffset);
				// 找到的HtmlNode节点 已经存在了 class 属性 那么直接退回即可
				if (existClassAttrubute != -1) {
					return null;
				}

				returnTag = {
					metaContentData: sourceContent,
					tagName: tagName,
					tagBeginIndex: index,
					tagEndIndex: closeTagEndIndex,
					locateContent: locateContent,
					locateBeginIndex: locateFindIndex,
					locateEndIndex: locateFindIndex + locateContent.length - 1,
					locateHtmlNode: nowHtmlNode
				};

				return returnTag;
			}
		}
	}

	return null;
}

/**
 * 为html节点 标签添加属性
 * @param {*} htmlNode     待添加的htmlNode节点
 * @param {*} attrubuteMap 添加的属性Map
 * @returns 返回当前HtmlNode节点内容替换后的信息
 */
function htmlNodeReplaceContent(htmlNode, attrubuteMap) {
	// 未找到指定HtmlNode返回null
	if (!htmlNode) {
		return null;
	}

	let metaContentData = htmlNode.metaContentData;

	// 静态的前缀部分(不会改变)
	let staticPrefixPart = metaContentData.substring(0, htmlNode.tagBeginIndex);
	// 目标修改 中间部分
	let targetMiddlePart = metaContentData.substring(htmlNode.tagBeginIndex, htmlNode.tagEndIndex);
	// 静态的后缀部分(不会改变)
	let staticSuffixPart = metaContentData.substring(htmlNode.tagEndIndex);

	console.log('staticPrefixPart==>', staticPrefixPart);
	console.log('targetMiddlePart==>', targetMiddlePart);
	console.log('staticSuffixPart==>', staticSuffixPart);

   



}

export default {
	// ====== Const Field ==========
	INNER_TARGET_TAG_ARR,
	HELP_LOCATE_CONTENT,
	INNER_TARGET_TAG_GROUP_CLASS_NAME_ARR,

	// =======  Methods  ===========
	findInnerTag,
	htmlNodeReplaceContent
};
