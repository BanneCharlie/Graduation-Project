import Editor, {ControlType, ElementType, splitText,} from "@hufe921/canvas-editor";
import { debounce, nextTick, scrollIntoView } from './utils/index.ts'
import { formatPrismToken } from './utils/prism'

export function Init  (content) {
    const isApple =
        typeof navigator !== 'undefined' && /Mac OS X/.test(navigator.userAgent);

    const editorElement = document.querySelector('.canvas-editor');

    if (!editorElement) {
        console.error('Element with class .canvas-editor not found.');
        return;
    }

    const RowFlex = {
        CENTER: 'center',
        LEFT: 'left',
        RIGHT: 'right'
    };

    const  commentList = []

    const instance = new Editor(
        editorElement,
        {
            header: content.header,

            main: content.main,

            footer: content.footer,
        }, // 数据
        {
            margins: [50, 50, 50, 50],
            watermark: {
                data: '',
                size: 120
            }, // 水印
            pageNumber: {
                //format: '第{pageNo}页/共{pageCount}页',
                disabled: true
            },
            placeholder: {
                data: '请输入正文'
            },
            zone: {
                tipDisabled: false
            },
            maskMargin: [60, 0, 30, 0], // 菜单栏高度60，底部工具栏30为遮盖层
            //defaultSize: 10, // 默认字体大小
            table:{
              tdPadding:[0, 0, 0, 1],
              defaultTrMinHeight:12,
            }

        } // 可选择项
    );

    Reflect.set(window, 'editor', instance);

// 1.菜单弹窗销毁
    window.addEventListener('click', function (evt) {
        const visibleDom = document.querySelector('.visible');
        if (!visibleDom || visibleDom.contains(evt.target)) return;
        visibleDom.classList.remove('visible');
    }, {
        capture: true
    });

// 2. | 撤销 | 重做 | 格式刷 | 清除格式 |
    const undoDom = document.querySelector('.menu-item__undo');
    undoDom.title = `撤销(${isApple ? '⌘' : 'Ctrl'}+Z)`;
    undoDom.onclick = function () {
        console.log('undo');
        instance.command.executeUndo();
    };

    const redoDom = document.querySelector('.menu-item__redo');
    redoDom.title = `重做(${isApple ? '⌘' : 'Ctrl'}+Y)`;
    redoDom.onclick = function () {
        console.log('redo');
        instance.command.executeRedo();
    };

    const painterDom = document.querySelector('.menu-item__painter');

    let isFirstClick = true;
    let painterTimeout;
    painterDom.onclick = function () {
        if (isFirstClick) {
            isFirstClick = false;
            painterTimeout = window.setTimeout(() => {
                console.log('painter-click');
                isFirstClick = true;
                instance.command.executePainter({
                    isDblclick: false
                });
            }, 200);
        } else {
            window.clearTimeout(painterTimeout);
        }
    };

    painterDom.ondblclick = function () {
        console.log('painter-dblclick');
        isFirstClick = true;
        window.clearTimeout(painterTimeout);
        instance.command.executePainter({
            isDblclick: true
        });
    };

    document.querySelector('.menu-item__format').onclick = function () {
        console.log('format');
        instance.command.executeFormat();
    };

//3. | 字体 | 字体变大 | 字体变小 | 加粗 | 斜体 | 下划线 | 删除线 | 上标 | 下标 | 字体颜色 | 背景色 |
// 字体
    const fontDom = document.querySelector('.menu-item__font');
    const fontSelectDom = fontDom.querySelector('.select');
    const fontOptionDom = fontDom.querySelector('.options');
    fontDom.onclick = function () {
        console.log('font');
        fontOptionDom.classList.toggle('visible');
    };
    fontOptionDom.onclick = function (evt) {
        const li = evt.target;
        instance.command.executeFont(li.dataset.family);
    };

// 字号设置
    const sizeSetDom = document.querySelector('.menu-item__size');
    const sizeSelectDom = sizeSetDom.querySelector('.select');
    const sizeOptionDom = sizeSetDom.querySelector('.options');
    sizeSetDom.title = `设置字号`;
    sizeSetDom.onclick = function () {
        console.log('size');
        sizeOptionDom.classList.toggle('visible');
    };
    sizeOptionDom.onclick = function (evt) {
        const li = evt.target;
        instance.command.executeSize(Number(li.dataset.size));
    };

// 增大字号
    const sizeAddDom = document.querySelector('.menu-item__size-add');
    sizeAddDom.title = `增大字号(${isApple ? '⌘' : 'Ctrl'}+[)`;
    sizeAddDom.onclick = function () {
        console.log('size-add');
        instance.command.executeSizeAdd();
    };

// 减小字号
    const sizeMinusDom = document.querySelector('.menu-item__size-minus');
    sizeMinusDom.title = `减小字号(${isApple ? '⌘' : 'Ctrl'}+])`;
    sizeMinusDom.onclick = function () {
        console.log('size-minus');
        instance.command.executeSizeMinus();
    };

// 加粗
    const boldDom = document.querySelector('.menu-item__bold');
    boldDom.title = `加粗(${isApple ? '⌘' : 'Ctrl'}+B)`;
    boldDom.onclick = function () {
        console.log('bold');
        instance.command.executeBold();
    };

// 斜体
    const italicDom = document.querySelector('.menu-item__italic');
    italicDom.title = `斜体(${isApple ? '⌘' : 'Ctrl'}+I)`;
    italicDom.onclick = function () {
        console.log('italic');
        instance.command.executeItalic();
    };

// 下划线
    const underlineDom = document.querySelector('.menu-item__underline');
    underlineDom.title = `下划线(${isApple ? '⌘' : 'Ctrl'}+U)`;
    const underlineOptionDom = underlineDom.querySelector('.options');
    underlineDom.querySelector('.select').onclick = function () {
        underlineOptionDom.classList.toggle('visible');
    };
    underlineDom.querySelector('i').onclick = function () {
        console.log('underline');
        instance.command.executeUnderline();
        underlineOptionDom.classList.remove('visible');
    };
    underlineDom.querySelector('ul').onmousedown = function (evt) {
        const li = evt.target;
        const decorationStyle = li.dataset.decorationStyle;
        instance.command.executeUnderline({
            style: decorationStyle
        });
    };

// 删除线
    const strikeoutDom = document.querySelector('.menu-item__strikeout');
    strikeoutDom.onclick = function () {
        console.log('strikeout');
        instance.command.executeStrikeout();
    };

// 上标
    const superscriptDom = document.querySelector('.menu-item__superscript');
    superscriptDom.title = `上标(${isApple ? '⌘' : 'Ctrl'}+Shift+,)`;
    superscriptDom.onclick = function () {
        console.log('superscript');
        instance.command.executeSuperscript();
    };

// 下标
    const subscriptDom = document.querySelector('.menu-item__subscript');
    subscriptDom.title = `下标(${isApple ? '⌘' : 'Ctrl'}+Shift+.)`;
    subscriptDom.onclick = function () {
        console.log('subscript');
        instance.command.executeSubscript();
    };

// 字体颜色
    const colorControlDom = document.querySelector('#color');
    colorControlDom.oninput = function () {
        instance.command.executeColor(colorControlDom.value);
    };
    const colorDom = document.querySelector('.menu-item__color');
    const colorSpanDom = colorDom.querySelector('span');
    colorDom.onclick = function () {
        console.log('color');
        colorControlDom.click();
    };

// 背景色
    const highlightControlDom = document.querySelector('#highlight');
    highlightControlDom.oninput = function () {
        instance.command.executeHighlight(highlightControlDom.value);
    };
    const highlightDom = document.querySelector('.menu-item__highlight');
    const highlightSpanDom = highlightDom.querySelector('span');
    highlightDom.onclick = function () {
        console.log('highlight');
        highlightControlDom?.click();
    };

// 标题设置
    const titleDom = document.querySelector('.menu-item__title');
    const titleSelectDom = titleDom.querySelector('.select');
    const titleOptionDom = titleDom.querySelector('.options');
    titleOptionDom.querySelectorAll('li').forEach((li, index) => {
        li.title = `Ctrl+${isApple ? 'Option' : 'Alt'}+${index}`;
    });
    titleDom.onclick = function () {
        console.log('title');
        titleOptionDom.classList.toggle('visible');
    };
    titleOptionDom.onclick = function (evt) {
        const li = evt.target;
        const level = li.dataset.level;
        instance.command.executeTitle(level || null);
    };

// 文本对齐
    const leftDom = document.querySelector('.menu-item__left');
    leftDom.title = `左对齐(${isApple ? '⌘' : 'Ctrl'}+L)`;
    leftDom.onclick = function () {
        console.log('left');
        instance.command.executeRowFlex(RowFlex.LEFT);
    };

    const centerDom = document.querySelector('.menu-item__center');
    centerDom.title = `居中对齐(${isApple ? '⌘' : 'Ctrl'}+E)`;
    centerDom.onclick = function () {
        console.log('center');
        instance.command.executeRowFlex(RowFlex.CENTER);
    };

    const rightDom = document.querySelector('.menu-item__right');
    rightDom.title = `右对齐(${isApple ? '⌘' : 'Ctrl'}+R)`;
    rightDom.onclick = function () {
        console.log('right');
        instance.command.executeRowFlex(RowFlex.RIGHT);
    };

    const alignmentDom = document.querySelector('.menu-item__alignment');
    alignmentDom.title = `两端对齐(${isApple ? '⌘' : 'Ctrl'}+J)`;
    alignmentDom.onclick = function () {
        console.log('alignment');
        instance.command.executeRowFlex(RowFlex.ALIGNMENT);
    };

    const justifyDom = document.querySelector('.menu-item__justify');
    justifyDom.title = `分散对齐(${isApple ? '⌘' : 'Ctrl'}+Shift+J)`;
    justifyDom.onclick = function () {
      console.log('justify');
      instance.command.executeRowFlex('justify');
    };

// 行间距
    const rowMarginDom = document.querySelector('.menu-item__row-margin');
    const rowOptionDom = rowMarginDom.querySelector('.options');
    rowMarginDom.onclick = function () {
        console.log('row-margin');
        rowOptionDom.classList.toggle('visible');
    };
    rowOptionDom.onclick = function (evt) {
        const li = evt.target;
        instance.command.executeRowMargin(Number(li.dataset.rowmargin));
    };

// 列表
    const listDom = document.querySelector('.menu-item__list');
    listDom.title = `列表(${isApple ? '⌘' : 'Ctrl'}+Shift+U)`;
    const listOptionDom = listDom.querySelector('.options');
    listDom.onclick = function () {
        console.log('list');
        listOptionDom.classList.toggle('visible');
    };
    listOptionDom.onclick = function (evt) {
        const li = evt.target;
        const listType = li.dataset.listType || null;
        const listStyle = li.dataset.listStyle;
        instance.command.executeList(listType, listStyle);
    };

// 4. | 表格 | 图片 | 超链接 | 分割线 | 水印 | 代码块 | 分隔符 | 控件 | 复选框 | LaTeX | 日期选择器
    const tableDom = document.querySelector('.menu-item__table');
    const tablePanelContainer = document.querySelector('.menu-item__table__collapse');
    const tableClose = document.querySelector('.table-close');
    const tableTitle = document.querySelector('.table-select');
    const tablePanel = document.querySelector('.table-panel');

// Draw rows and columns
    const tableCellList = [];
    for (let i = 0; i < 10; i++) {
        const tr = document.createElement('tr');
        tr.classList.add('table-row');
        const trCellList = [];
        for (let j = 0; j < 10; j++) {
            const td = document.createElement('td');
            td.classList.add('table-cel');
            tr.appendChild(td);
            trCellList.push(td);
        }
        tablePanel.appendChild(tr);
        tableCellList.push(trCellList);
    }

    let colIndex = 0;
    let rowIndex = 0;

// Remove all table cell selections
    function removeAllTableCellSelect() {
        tableCellList.forEach(tr => {
            tr.forEach(td => td.classList.remove('active'));
        });
    }

// Set table title content
    function setTableTitle(payload) {
        tableTitle.innerText = payload;
    }

// Restore initial state
    function recoveryTable() {
        removeAllTableCellSelect();
        setTableTitle('插入');
        colIndex = 0;
        rowIndex = 0;
        tablePanelContainer.style.display = 'none';
    }

    tableDom.onclick = function () {
        console.log('table');
        tablePanelContainer.style.display = 'block';
    };

    tablePanel.onmousemove = function (evt) {
        const celSize = 16;
        const rowMarginTop = 10;
        const celMarginRight = 6;
        const {offsetX, offsetY} = evt;
        removeAllTableCellSelect();
        colIndex = Math.ceil(offsetX / (celSize + celMarginRight)) || 1;
        rowIndex = Math.ceil(offsetY / (celSize + rowMarginTop)) || 1;
        tableCellList.forEach((tr, trIndex) => {
            tr.forEach((td, tdIndex) => {
                if (tdIndex < colIndex && trIndex < rowIndex) {
                    td.classList.add('active');
                }
            });
        });
        setTableTitle(`${rowIndex}×${colIndex}`);
    };

    tableClose.onclick = function () {
        recoveryTable();
    };

    tablePanel.onclick = function () {
        instance.command.executeInsertTable(rowIndex, colIndex);
        recoveryTable();
    };

    const imageDom = document.querySelector('.menu-item__image');
    const imageFileDom = document.querySelector('#image');
    imageDom.onclick = function () {
        imageFileDom.click();
    };
    imageFileDom.onchange = function () {
        const file = imageFileDom.files[0];
        const fileReader = new FileReader();
        fileReader.readAsDataURL(file);
        fileReader.onload = function () {
            const image = new Image();
            const value = fileReader.result;
            image.src = value;
            image.onload = function () {
                instance.command.executeImage({
                    value,
                    width: image.width,
                    height: image.height
                });
                imageFileDom.value = '';
            };
        };
    };

    const hyperlinkDom = document.querySelector('.menu-item__hyperlink');
    hyperlinkDom.onclick = function () {
        console.log('hyperlink');
        new Dialog({
            title: '超链接',
            data: [
                {
                    type: 'text',
                    label: '文本',
                    name: 'name',
                    required: true,
                    placeholder: '请输入文本',
                    value: instance.command.getRangeText()
                },
                {
                    type: 'text',
                    label: '链接',
                    name: 'url',
                    required: true,
                    placeholder: '请输入链接'
                }
            ],
            onConfirm: payload => {
                const name = payload.find(p => p.name === 'name')?.value;
                if (!name) return;
                const url = payload.find(p => p.name === 'url')?.value;
                if (!url) return;
                instance.command.executeHyperlink({
                    type: ElementType.HYPERLINK,
                    value: '',
                    url,
                    valueList: splitText(name).map(n => ({
                        value: n,
                        size: 16
                    }))
                });
            }
        });
    };

    const separatorDom = document.querySelector('.menu-item__separator');
    const separatorOptionDom = separatorDom.querySelector('.options');
    separatorDom.onclick = function () {
        console.log('separator');
        separatorOptionDom.classList.toggle('visible');
    };
    separatorOptionDom.onmousedown = function (evt) {
        let payload = [];
        const li = evt.target;
        const separatorDash = li.dataset.separator?.split(',').map(Number);
        if (separatorDash) {
            const isSingleLine = separatorDash.every(d => d === 0);
            if (!isSingleLine) {
                payload = separatorDash;
            }
        }
        instance.command.executeSeparator(payload);
    };

    const pageBreakDom = document.querySelector('.menu-item__page-break');
    pageBreakDom.onclick = function () {
        console.log('pageBreak');
        instance.command.executePageBreak();
    };

    const watermarkDom = document.querySelector('.menu-item__watermark');
    const watermarkOptionDom = watermarkDom.querySelector('.options');
    watermarkDom.onclick = function () {
        console.log('watermark');
        watermarkOptionDom.classList.toggle('visible');
    };
    watermarkOptionDom.onmousedown = function (evt) {
        const li = evt.target;
        const menu = li.dataset.menu;
        watermarkOptionDom.classList.toggle('visible');
        if (menu === 'add') {
            new Dialog({
                title: '水印',
                data: [
                    {
                        type: 'text',
                        label: '内容',
                        name: 'data',
                        required: true,
                        placeholder: '请输入内容'
                    },
                    {
                        type: 'color',
                        label: '颜色',
                        name: 'color',
                        required: true,
                        value: '#AEB5C0'
                    },
                    {
                        type: 'number',
                        label: '字体大小',
                        name: 'size',
                        required: true,
                        value: '120'
                    }
                ],
                onConfirm: payload => {
                    const nullableIndex = payload.findIndex(p => !p.value);
                    if (nullableIndex !== -1) return;
                    const watermark = payload.reduce((pre, cur) => {
                        pre[cur.name] = cur.value;
                        return pre;
                    }, {});
                    instance.command.executeAddWatermark({
                        data: watermark.data,
                        color: watermark.color,
                        size: Number(watermark.size)
                    });
                }
            });
        } else {
            instance.command.executeDeleteWatermark();
        }
    };

    const codeblockDom = document.querySelector('.menu-item__codeblock');
    codeblockDom.onclick = function () {
        console.log('codeblock');
        new Dialog({
            title: '代码块',
            data: [
                {
                    type: 'textarea',
                    name: 'codeblock',
                    placeholder: '请输入代码',
                    width: 500,
                    height: 300
                }
            ],
            onConfirm: payload => {
                const codeblock = payload.find(p => p.name === 'codeblock')?.value;
                if (!codeblock) return;
                const tokenList = prism.tokenize(codeblock, prism.languages.javascript);
                const formatTokenList = formatPrismToken(tokenList);
                const elementList = [];
                for (let i = 0; i < formatTokenList.length; i++) {
                    const formatToken = formatTokenList[i];
                    const tokenStringList = splitText(formatToken.content);
                    for (let j = 0; j < tokenStringList.length; j++) {
                        const value = tokenStringList[j];
                        const element = {
                            value
                        };
                        if (formatToken.color) {
                            element.color = formatToken.color;
                        }
                        if (formatToken.bold) {
                            element.bold = true;
                        }
                        if (formatToken.italic) {
                            element.italic = true;
                        }
                        elementList.push(element);
                    }
                }
                elementList.unshift({
                    value: '\n'
                });
                instance.command.executeInsertElementList(elementList);
            }
        });
    };
    const controlDom = document.querySelector('.menu-item__control');
    const controlOptionDom = controlDom.querySelector('.options');
    controlDom.onclick = function () {
        console.log('control');
        controlOptionDom.classList.toggle('visible');
    };
    controlOptionDom.onmousedown = function (evt) {
        controlOptionDom.classList.toggle('visible');
        const li = evt.target;
        const type = li.dataset.control;
        switch (type) {
            case ControlType.TEXT:
                new Dialog({
                    title: '文本控件',
                    data: [
                        {
                            type: 'text',
                            label: '占位符',
                            name: 'placeholder',
                            required: true,
                            placeholder: '请输入占位符'
                        },
                        {
                            type: 'text',
                            label: '默认值',
                            name: 'value',
                            placeholder: '请输入默认值'
                        }
                    ],
                    onConfirm: payload => {
                        const placeholder = payload.find(p => p.name === 'placeholder')?.value;
                        if (!placeholder) return;
                        const value = payload.find(p => p.name === 'value')?.value || '';
                        instance.command.executeInsertElementList([
                            {
                                type: ElementType.CONTROL,
                                value: '',
                                control: {
                                    type,
                                    value: value
                                        ? [
                                            {
                                                value
                                            }
                                        ]
                                        : null,
                                    placeholder
                                }
                            }
                        ]);
                    }
                });
                break;
            case ControlType.SELECT:
                new Dialog({
                    title: '列举控件',
                    data: [
                        {
                            type: 'text',
                            label: '占位符',
                            name: 'placeholder',
                            required: true,
                            placeholder: '请输入占位符'
                        },
                        {
                            type: 'text',
                            label: '默认值',
                            name: 'code',
                            placeholder: '请输入默认值'
                        },
                        {
                            type: 'textarea',
                            label: '值集',
                            name: 'valueSets',
                            required: true,
                            height: 100,
                            placeholder: `请输入值集JSON，例：\n[{\n"value":"有",\n"code":"98175"\n}]`
                        }
                    ],
                    onConfirm: payload => {
                        const placeholder = payload.find(p => p.name === 'placeholder')?.value;
                        if (!placeholder) return;
                        const valueSets = payload.find(p => p.name === 'valueSets')?.value;
                        if (!valueSets) return;
                        const code = payload.find(p => p.name === 'code')?.value;
                        instance.command.executeInsertElementList([
                            {
                                type: ElementType.CONTROL,
                                value: '',
                                control: {
                                    type,
                                    code,
                                    value: null,
                                    placeholder,
                                    valueSets: JSON.parse(valueSets)
                                }
                            }
                        ]);
                    }
                });
                break;
            case ControlType.CHECKBOX:
                new Dialog({
                    title: '复选框控件',
                    data: [
                        {
                            type: 'text',
                            label: '默认值',
                            name: 'code',
                            placeholder: '请输入默认值，多个值以英文逗号分割'
                        },
                        {
                            type: 'textarea',
                            label: '值集',
                            name: 'valueSets',
                            required: true,
                            height: 100,
                            placeholder: `请输入值集JSON，例：\n[{\n"value":"有",\n"code":"98175"\n}]`
                        }
                    ],
                    onConfirm: payload => {
                        const valueSets = payload.find(p => p.name === 'valueSets')?.value;
                        if (!valueSets) return;
                        const code = payload.find(p => p.name === 'code')?.value;
                        instance.command.executeInsertElementList([
                            {
                                type: ElementType.CONTROL,
                                value: '',
                                control: {
                                    type,
                                    code,
                                    value: null,
                                    valueSets: JSON.parse(valueSets)
                                }
                            }
                        ]);
                    }
                });
                break;
            case ControlType.RADIO:
                new Dialog({
                    title: '单选框控件',
                    data: [
                        {
                            type: 'text',
                            label: '默认值',
                            name: 'code',
                            placeholder: '请输入默认值'
                        },
                        {
                            type: 'textarea',
                            label: '值集',
                            name: 'valueSets',
                            required: true,
                            height: 100,
                            placeholder: `请输入值集JSON，例：\n[{\n"value":"有",\n"code":"98175"\n}]`
                        }
                    ],
                    onConfirm: payload => {
                        const valueSets = payload.find(p => p.name === 'valueSets')?.value;
                        if (!valueSets) return;
                        const code = payload.find(p => p.name === 'code')?.value;
                        instance.command.executeInsertElementList([
                            {
                                type: ElementType.CONTROL,
                                value: '',
                                control: {
                                    type,
                                    code,
                                    value: null,
                                    valueSets: JSON.parse(valueSets)
                                }
                            }
                        ]);
                    }
                });
                break;
            case ControlType.DATE:
                new Dialog({
                    title: '日期控件',
                    data: [
                        {
                            type: 'text',
                            label: '占位符',
                            name: 'placeholder',
                            required: true,
                            placeholder: '请输入占位符'
                        },
                        {
                            type: 'text',
                            label: '默认值',
                            name: 'value',
                            placeholder: '请输入默认值'
                        },
                        {
                            type: 'select',
                            label: '日期格式',
                            name: 'dateFormat',
                            value: 'yyyy-MM-dd hh:mm:ss',
                            required: true,
                            options: [
                                {
                                    label: 'yyyy-MM-dd hh:mm:ss',
                                    value: 'yyyy-MM-dd hh:mm:ss'
                                },
                                {
                                    label: 'yyyy-MM-dd',
                                    value: 'yyyy-MM-dd'
                                }
                            ]
                        }
                    ],
                    onConfirm: payload => {
                        const placeholder = payload.find(p => p.name === 'placeholder')?.value;
                        if (!placeholder) return;
                        const value = payload.find(p => p.name === 'value')?.value || '';
                        const dateFormat = payload.find(p => p.name === 'dateFormat')?.value || '';
                        instance.command.executeInsertElementList([
                            {
                                type: ElementType.CONTROL,
                                value: '',
                                control: {
                                    type,
                                    dateFormat,
                                    value: value
                                        ? [
                                            {
                                                value
                                            }
                                        ]
                                        : null,
                                    placeholder
                                }
                            }
                        ]);
                    }
                });
                break;
            default:
                break;
        }
    };

    const checkboxDom = document.querySelector('.menu-item__checkbox');
    checkboxDom.onclick = function () {
        console.log('checkbox');
        instance.command.executeInsertElementList([
            {
                type: ElementType.CHECKBOX,
                checkbox: {
                    value: false
                },
                value: ''
            }
        ]);
    };

    const radioDom = document.querySelector('.menu-item__radio');
    radioDom.onclick = function () {
        console.log('radio');
        instance.command.executeInsertElementList([
            {
                type: ElementType.RADIO,
                checkbox: {
                    value: false
                },
                value: ''
            }
        ]);
    };

    const latexDom = document.querySelector('.menu-item__latex');
    latexDom.onclick = function () {
        console.log('LaTeX');
        new Dialog({
            title: 'LaTeX',
            data: [
                {
                    type: 'textarea',
                    height: 100,
                    name: 'value',
                    placeholder: '请输入LaTeX文本'
                }
            ],
            onConfirm: payload => {
                const value = payload.find(p => p.name === 'value')?.value;
                if (!value) return;
                instance.command.executeInsertElementList([
                    {
                        type: ElementType.LATEX,
                        value
                    }
                ]);
            }
        });
    };

    const dateDom = document.querySelector('.menu-item__date');
    const dateDomOptionDom = dateDom.querySelector('.options');
    dateDom.onclick = function () {
        console.log('date');
        dateDomOptionDom.classList.toggle('visible');
        // Adjust position
        const bodyRect = document.body.getBoundingClientRect();
        const dateDomOptionRect = dateDomOptionDom.getBoundingClientRect();
        if (dateDomOptionRect.left + dateDomOptionRect.width > bodyRect.width) {
            dateDomOptionDom.style.right = '0px';
            dateDomOptionDom.style.left = 'unset';
        } else {
            dateDomOptionDom.style.right = 'unset';
            dateDomOptionDom.style.left = '0px';
        }
        // Current date
        const date = new Date();
        const year = date.getFullYear().toString();
        const month = (date.getMonth() + 1).toString().padStart(2, '0');
        const day = date.getDate().toString().padStart(2, '0');
        const hour = date.getHours().toString().padStart(2, '0');
        const minute = date.getMinutes().toString().padStart(2, '0');
        const second = date.getSeconds().toString().padStart(2, '0');
        const dateString = `${year}-${month}-${day}`;
        const dateTimeString = `${dateString} ${hour}:${minute}:${second}`;
        dateDomOptionDom.querySelector('li:first-child').innerText = dateString;
        dateDomOptionDom.querySelector('li:last-child').innerText = dateTimeString;
    };
    dateDomOptionDom.onmousedown = function (evt) {
        const li = evt.target;
        const dateFormat = li.dataset.format;
        dateDomOptionDom.classList.toggle('visible');
        instance.command.executeInsertElementList([
            {
                type: ElementType.DATE,
                value: '',
                dateFormat,
                valueList: [
                    {
                        value: li.innerText.trim()
                    }
                ]
            }
        ]);
    };

    const blockDom = document.querySelector('.menu-item__block');
    blockDom.onclick = function () {
        console.log('block');
        new Dialog({
            title: '内容块',
            data: [
                {
                    type: 'select',
                    label: '类型',
                    name: 'type',
                    value: 'iframe',
                    required: true,
                    options: [
                        {
                            label: '网址',
                            value: 'iframe'
                        },
                        {
                            label: '视频',
                            value: 'video'
                        }
                    ]
                },
                {
                    type: 'number',
                    label: '宽度',
                    name: 'width',
                    placeholder: '请输入宽度（默认页面内宽度）'
                },
                {
                    type: 'number',
                    label: '高度',
                    name: 'height',
                    required: true,
                    placeholder: '请输入高度'
                },
                {
                    type: 'input',
                    label: '地址',
                    name: 'src',
                    required: false,
                    placeholder: '请输入地址'
                },
                {
                    type: 'textarea',
                    label: 'HTML',
                    height: 100,
                    name: 'srcdoc',
                    required: false,
                    placeholder: '请输入HTML代码（仅网址类型有效）'
                }
            ],
            onConfirm: payload => {
                const type = payload.find(p => p.name === 'type')?.value;
                if (!type) return;
                const width = payload.find(p => p.name === 'width')?.value;
                const height = payload.find(p => p.name === 'height')?.value;
                if (!height) return;
                // Either src or srcdoc must exist
                const src = payload.find(p => p.name === 'src')?.value;
                const srcdoc = payload.find(p => p.name === 'srcdoc')?.value;
                const block = {
                    type: type
                };
                if (block.type === 'iframe') {
                    if (!src && !srcdoc) return;
                    block.iframeBlock = {
                        src,
                        srcdoc
                    };
                } else if (block.type === 'video') {
                    if (!src) return;
                    block.videoBlock = {
                        src
                    };
                }
                const blockElement = {
                    type: 'BLOCK',
                    value: '',
                    height: Number(height),
                    block
                };
                if (width) {
                    blockElement.width = Number(width);
                }
                instance.command.executeInsertElementList([blockElement]);
            }
        });
    };

// 5. | 搜索&替换 | 打印 |
    const searchCollapseDom = document.querySelector('.menu-item__search__collapse');
    const searchInputDom = document.querySelector('.menu-item__search__collapse__search input');
    const replaceInputDom = document.querySelector('.menu-item__search__collapse__replace input');
    const searchDom = document.querySelector('.menu-item__search');
    searchDom.title = `搜索与替换(${isApple ? '⌘' : 'Ctrl'}+F)`;
    const searchResultDom = searchCollapseDom.querySelector('.search-result');

    function setSearchResult() {
        const result = instance.command.getSearchNavigateInfo();
        if (result) {
            const {index, count} = result;
            searchResultDom.innerText = `${index}/${count}`;
        } else {
            searchResultDom.innerText = '';
        }
    }

    searchDom.onclick = function () {
        console.log('search');
        searchCollapseDom.style.display = 'block';
        const bodyRect = document.body.getBoundingClientRect();
        const searchRect = searchDom.getBoundingClientRect();
        const searchCollapseRect = searchCollapseDom.getBoundingClientRect();
        if (searchRect.left + searchCollapseRect.width > bodyRect.width) {
            searchCollapseDom.style.right = '0px';
            searchCollapseDom.style.left = 'unset';
        } else {
            searchCollapseDom.style.right = 'unset';
        }
        searchInputDom.focus();
    }

    searchCollapseDom.querySelector('span').onclick = function () {
        searchCollapseDom.style.display = 'none';
        searchInputDom.value = '';
        replaceInputDom.value = '';
        instance.command.executeSearch(null);
        setSearchResult();
    }

    searchInputDom.oninput = function () {
        instance.command.executeSearch(searchInputDom.value || null);
        setSearchResult();
    }

    searchInputDom.onkeydown = function (evt) {
        if (evt.key === 'Enter') {
            instance.command.executeSearch(searchInputDom.value || null);
            setSearchResult();
        }
    }

    searchCollapseDom.querySelector('button').onclick = function () {
        const searchValue = searchInputDom.value;
        const replaceValue = replaceInputDom.value;
        if (searchValue && replaceValue && searchValue !== replaceValue) {
            instance.command.executeReplace(replaceValue);
        }
    }

    searchCollapseDom.querySelector('.arrow-left').onclick = function () {
        instance.command.executeSearchNavigatePre();
        setSearchResult();
    }

    searchCollapseDom.querySelector('.arrow-right').onclick = function () {
        instance.command.executeSearchNavigateNext();
        setSearchResult();
    }

    const printDom = document.querySelector('.menu-item__print');
    printDom.title = `打印(${isApple ? '⌘' : 'Ctrl'}+P)`;
    printDom.onclick = function () {
        console.log('print');
        instance.command.executePrint();
    }

// 6. 目录显隐 | 页面模式 | 纸张缩放 | 纸张大小 | 纸张方向 | 页边距 | 全屏 | 设置
    const editorOptionDom = document.querySelector('.editor-option');
    editorOptionDom.onclick = function () {
        const options = instance.command.getOptions();
        new Dialog({
            title: '编辑器配置',
            data: [
                {
                    type: 'textarea',
                    name: 'option',
                    width: 350,
                    height: 300,
                    required: true,
                    value: JSON.stringify(options, null, 2),
                    placeholder: '请输入编辑器配置'
                }
            ],
            onConfirm: payload => {
                const newOptionValue = payload.find(p => p.name === 'option').value;
                if (!newOptionValue) return;
                const newOption = JSON.parse(newOptionValue);
                instance.command.executeUpdateOptions(newOption);
            }
        });
    }

    async function updateCatalog() {
        const catalog = await instance.command.getCatalog();
        const catalogMainDom = document.querySelector('.catalog__main');
        catalogMainDom.innerHTML = '';
        if (catalog) {
            const appendCatalog = (parent, catalogItems) => {
                for (let c = 0; c < catalogItems.length; c++) {
                    const catalogItem = catalogItems[c];
                    const catalogItemDom = document.createElement('div');
                    catalogItemDom.classList.add('catalog-item');

                    // Render
                    const catalogItemContentDom = document.createElement('div');
                    catalogItemContentDom.classList.add('catalog-item__content');
                    const catalogItemContentSpanDom = document.createElement('span');
                    catalogItemContentSpanDom.innerText = catalogItem.name;
                    catalogItemContentDom.append(catalogItemContentSpanDom);

                    // Location
                    catalogItemContentDom.onclick = () => {
                        instance.command.executeLocationCatalog(catalogItem.id);
                    };
                    catalogItemDom.append(catalogItemContentDom);

                    if (catalogItem.subCatalog && catalogItem.subCatalog.length) {
                        appendCatalog(catalogItemDom, catalogItem.subCatalog);
                    }

                    // Append
                    parent.append(catalogItemDom);
                }
            };
            appendCatalog(catalogMainDom, catalog);
        }
    }

    let isCatalogShow = true;
    const catalogDom = document.querySelector('.catalog');
    const catalogModeDom = document.querySelector('.catalog-mode');
    const catalogHeaderCloseDom = document.querySelector('.catalog__header__close');
    const switchCatalog = () => {
        isCatalogShow = !isCatalogShow;
        if (isCatalogShow) {
            catalogDom.style.display = 'block';
            updateCatalog();
        } else {
            catalogDom.style.display = 'none';
        }
    };
    catalogModeDom.onclick = switchCatalog;
    catalogHeaderCloseDom.onclick = switchCatalog;

    const pageModeDom = document.querySelector('.page-mode');
    const pageModeOptionsDom = pageModeDom.querySelector('.options');
    pageModeDom.onclick = function () {
        pageModeOptionsDom.classList.toggle('visible');
    };
    pageModeOptionsDom.onclick = function (evt) {
        const li = evt.target;
        instance.command.executePageMode(li.dataset.pageMode);
    };

    document.querySelector('.page-scale-percentage').onclick = function () {
        console.log('page-scale-recovery');
        instance.command.executePageScaleRecovery();
    };

    document.querySelector('.page-scale-minus').onclick = function () {
        console.log('page-scale-minus');
        instance.command.executePageScaleMinus();
    };

    document.querySelector('.page-scale-add').onclick = function () {
        console.log('page-scale-add');
        instance.command.executePageScaleAdd();
    };

// Paper Size
    const paperSizeDom = document.querySelector('.paper-size');
    const paperSizeDomOptionsDom = paperSizeDom.querySelector('.options');
    paperSizeDom.onclick = function () {
        paperSizeDomOptionsDom.classList.toggle('visible');
    };
    paperSizeDomOptionsDom.onclick = function (evt) {
        const li = evt.target;
        const paperType = li.dataset.paperSize;
        const [width, height] = paperType.split('*').map(Number);
        instance.command.executePaperSize(width, height);

        // Paper status echo
        paperSizeDomOptionsDom.querySelectorAll('li').forEach(child => child.classList.remove('active'));
        li.classList.add('active');
    };
// 纸张方向
    const paperDirectionDom = document.querySelector('.paper-direction');
    const paperDirectionDomOptionsDom = paperDirectionDom.querySelector('.options');
    paperDirectionDom.onclick = function () {
        paperDirectionDomOptionsDom.classList.toggle('visible');
    };
    paperDirectionDomOptionsDom.onclick = function (evt) {
        const li = evt.target;
        const paperDirection = li.dataset.paperDirection;
        instance.command.executePaperDirection(paperDirection);
        // 纸张方向状态回显
        paperDirectionDomOptionsDom.querySelectorAll('li').forEach(child => child.classList.remove('active'));
        li.classList.add('active');
    };

// 页面边距
    const paperMarginDom = document.querySelector('.paper-margin');
    paperMarginDom.onclick = function () {
        const [topMargin, rightMargin, bottomMargin, leftMargin] = instance.command.getPaperMargin();
        new Dialog({
            title: '页边距',
            data: [
                {
                    type: 'text',
                    label: '上边距',
                    name: 'top',
                    required: true,
                    value: `${topMargin}`,
                    placeholder: '请输入上边距'
                },
                {
                    type: 'text',
                    label: '下边距',
                    name: 'bottom',
                    required: true,
                    value: `${bottomMargin}`,
                    placeholder: '请输入下边距'
                },
                {
                    type: 'text',
                    label: '左边距',
                    name: 'left',
                    required: true,
                    value: `${leftMargin}`,
                    placeholder: '请输入左边距'
                },
                {
                    type: 'text',
                    label: '右边距',
                    name: 'right',
                    required: true,
                    value: `${rightMargin}`,
                    placeholder: '请输入右边距'
                }
            ],
            onConfirm: payload => {
                const top = payload.find(p => p.name === 'top')?.value;
                if (!top) return;
                const bottom = payload.find(p => p.name === 'bottom')?.value;
                if (!bottom) return;
                const left = payload.find(p => p.name === 'left')?.value;
                if (!left) return;
                const right = payload.find(p => p.name === 'right')?.value;
                if (!right) return;
                instance.command.executeSetPaperMargin([Number(top), Number(right), Number(bottom), Number(left)]);
            }
        });
    };

// 全屏
    const fullscreenDom = document.querySelector('.fullscreen');
    fullscreenDom.onclick = toggleFullscreen;
    window.addEventListener('keydown', evt => {
        if (evt.key === 'F11') {
            toggleFullscreen();
            evt.preventDefault();
        }
    });
    document.addEventListener('fullscreenchange', () => {
        fullscreenDom.classList.toggle('exist');
    });

    function toggleFullscreen() {
        console.log('fullscreen');
        if (!document.fullscreenElement) {
            document.documentElement.requestFullscreen();
        } else {
            document.exitFullscreen();
        }
    }

// 7.编辑器使用模式
    let modeIndex = 0;
    const modeList = [
        {
            mode: 'EDIT', // EditorMode.EDIT
            name: '编辑模式'
        },
        {
            mode: 'CLEAN', // EditorMode.CLEAN
            name: '清洁模式'
        },
/*        {
            mode: 'READONLY', // EditorMode.READONLY
            name: '只读模式'
        },*/
        {
            mode: 'FORM', // EditorMode.FORM
            name: '表单模式'
        },
        {
            mode: 'PRINT', // EditorMode.PRINT
            name: '打印模式'
        }
    ];
    const modeElement = document.querySelector('.editor-mode');
    modeElement.onclick = function () {
        // 模式选择循环
        modeIndex === modeList.length - 1 ? (modeIndex = 0) : modeIndex++;
        // 设置模式
        const {name, mode} = modeList[modeIndex];
        modeElement.innerText = name;
        instance.command.executeMode(mode);
        // 设置菜单栏权限视觉反馈
        const isReadonly = mode === 'READONLY';
        const enableMenuList = ['search', 'print'];
        document.querySelectorAll('.menu-item>div').forEach(dom => {
            const menu = dom.dataset.menu;
            isReadonly && (!menu || !enableMenuList.includes(menu))
                ? dom.classList.add('disable')
                : dom.classList.remove('disable');
        });
    };

// 模拟批注
    const commentDom = document.querySelector('.comment');

    async function updateComment() {
        const groupIds = await instance.command.getGroupIds();
        for (const comment of commentList) {
            const activeCommentDom = commentDom.querySelector(`.comment-item[data-id='${comment.id}']`);
            // 编辑器是否存在对应成组id
            if (groupIds.includes(comment.id)) {
                // 当前dom是否存在-不存在则追加
                if (!activeCommentDom) {
                    const commentItem = document.createElement('div');
                    commentItem.classList.add('comment-item');
                    commentItem.setAttribute('data-id', comment.id);
                    commentItem.onclick = () => {
                        instance.command.executeLocationGroup(comment.id);
                    };
                    commentDom.append(commentItem);
                    // 选区信息
                    const commentItemTitle = document.createElement('div');
                    commentItemTitle.classList.add('comment-item__title');
                    commentItemTitle.append(document.createElement('span'));
                    const commentItemTitleContent = document.createElement('span');
                    commentItemTitleContent.innerText = comment.rangeText;
                    commentItemTitle.append(commentItemTitleContent);
                    const closeDom = document.createElement('i');
                    closeDom.onclick = () => {
                        instance.command.executeDeleteGroup(comment.id);
                    };
                    commentItemTitle.append(closeDom);
                    commentItem.append(commentItemTitle);
                    // 基础信息
                    const commentItemInfo = document.createElement('div');
                    commentItemInfo.classList.add('comment-item__info');
                    const commentItemInfoName = document.createElement('span');
                    commentItemInfoName.innerText = comment.userName;
                    const commentItemInfoDate = document.createElement('span');
                    commentItemInfoDate.innerText = comment.createdDate;
                    commentItemInfo.append(commentItemInfoName);
                    commentItemInfo.append(commentItemInfoDate);
                    commentItem.append(commentItemInfo);
                    // 详细评论
                    const commentItemContent = document.createElement('div');
                    commentItemContent.classList.add('comment-item__content');
                    commentItemContent.innerText = comment.content;
                    commentItem.append(commentItemContent);
                    commentDom.append(commentItem);
                }
            } else {
                // 编辑器内不存在对应成组id则dom则移除
                activeCommentDom?.remove();
            }
        }
    }

// 8.内部事件监听
    instance.listener.rangeStyleChange = function (payload) {
        // 控件类型
        payload.type === 'SUBSCRIPT'
            ? subscriptDom.classList.add('active')
            : subscriptDom.classList.remove('active');
        payload.type === 'SUPERSCRIPT'
            ? superscriptDom.classList.add('active')
            : superscriptDom.classList.remove('active');
        payload.type === 'SEPARATOR'
            ? separatorDom.classList.add('active')
            : separatorDom.classList.remove('active');

        separatorOptionDom.querySelectorAll('li').forEach(li => li.classList.remove('active'));
        if (payload.type === 'SEPARATOR') {
            const separator = payload.dashArray.join(',') || '0,0';
            const curSeparatorDom = separatorOptionDom.querySelector(`[data-separator='${separator}']`);
            if (curSeparatorDom) {
                curSeparatorDom.classList.add('active');
            }
        }

        // 富文本
        fontOptionDom.querySelectorAll('li').forEach(li => li.classList.remove('active'));
        const curFontDom = fontOptionDom.querySelector(`[data-family='${payload.font}']`);
        if (curFontDom) {
            fontSelectDom.innerText = curFontDom.innerText;
            fontSelectDom.style.fontFamily = payload.font;
            curFontDom.classList.add('active');
        }

        sizeOptionDom.querySelectorAll('li').forEach(li => li.classList.remove('active'));
        const curSizeDom = sizeOptionDom.querySelector(`[data-size='${payload.size}']`);
        if (curSizeDom) {
            sizeSelectDom.innerText = curSizeDom.innerText;
            curSizeDom.classList.add('active');
        } else {
            sizeSelectDom.innerText = `${payload.size}`;
        }

        payload.bold
            ? boldDom.classList.add('active')
            : boldDom.classList.remove('active');
        payload.italic
            ? italicDom.classList.add('active')
            : italicDom.classList.remove('active');
        payload.underline
            ? underlineDom.classList.add('active')
            : underlineDom.classList.remove('active');
        payload.strikeout
            ? strikeoutDom.classList.add('active')
            : strikeoutDom.classList.remove('active');

        if (payload.color) {
            colorDom.classList.add('active');
            colorControlDom.value = payload.color;
            colorSpanDom.style.backgroundColor = payload.color;
        } else {
            colorDom.classList.remove('active');
            colorControlDom.value = '#000000';
            colorSpanDom.style.backgroundColor = '#000000';
        }

        if (payload.highlight) {
            highlightDom.classList.add('active');
            highlightControlDom.value = payload.highlight;
            highlightSpanDom.style.backgroundColor = payload.highlight;
        } else {
            highlightDom.classList.remove('active');
            highlightControlDom.value = '#ffff00';
            highlightSpanDom.style.backgroundColor = '#ffff00';
        }

        // 行布局
        leftDom.classList.remove('active');
        centerDom.classList.remove('active');
        rightDom.classList.remove('active');
        alignmentDom.classList.remove('active');
        justifyDom.classList.remove('active');

        if (payload.rowFlex && payload.rowFlex === 'right') {
            rightDom.classList.add('active');
        } else if (payload.rowFlex && payload.rowFlex === 'center') {
            centerDom.classList.add('active');
        } else if (payload.rowFlex && payload.rowFlex === 'alignment') {
            alignmentDom.classList.add('active');
        } else if (payload.rowFlex && payload.rowFlex === 'justify') {
            justifyDom.classList.add('active');
        } else {
            leftDom.classList.add('active');
        }

        // 行间距
        rowOptionDom.querySelectorAll('li').forEach(li => li.classList.remove('active'));
        const curRowMarginDom = rowOptionDom.querySelector(`[data-rowmargin='${payload.rowMargin}']`);
        curRowMarginDom.classList.add('active');

        // 功能
        payload.undo
            ? undoDom.classList.remove('no-allow')
            : undoDom.classList.add('no-allow');
        payload.redo
            ? redoDom.classList.remove('no-allow')
            : redoDom.classList.add('no-allow');
        payload.painter
            ? painterDom.classList.add('active')
            : painterDom.classList.remove('active');

        // 标题
        titleOptionDom.querySelectorAll('li').forEach(li => li.classList.remove('active'));
        if (payload.level) {
            const curTitleDom = titleOptionDom.querySelector(`[data-level='${payload.level}']`);
            titleSelectDom.innerText = curTitleDom.innerText;
            curTitleDom.classList.add('active');
        } else {
            titleSelectDom.innerText = '正文';
            titleOptionDom.querySelector('li:first-child').classList.add('active');
        }

        // 列表
        listOptionDom.querySelectorAll('li').forEach(li => li.classList.remove('active'));
        if (payload.listType) {
            listDom.classList.add('active');
            const listType = payload.listType === 'OL' ? 'DECIMAL' : payload.listType;
            const curListDom = listOptionDom.querySelector(`[data-list-type='${listType}'][data-list-style='${listStyle}']`);
            if (curListDom) {
                curListDom.classList.add('active');
            }
        } else {
            listDom.classList.remove('active');
        }

    }

// 控件变更监听
    instance.listener.controlChange = function (payload) {
        const disableMenusInControlContext = [
            'table',
            'hyperlink',
            'separator',
            'page-break',
            'control'
        ];
        // 菜单操作权限
        disableMenusInControlContext.forEach(menu => {
            const menuDom = document.querySelector(`.menu-item__${menu}`);
            if (menuDom) {
                payload
                    ? menuDom.classList.add('disable')
                    : menuDom.classList.remove('disable');
            }
        });
    };

// 页面模式变更监听
    instance.listener.pageModeChange = function (payload) {
        const activeMode = pageModeOptionsDom.querySelector(`[data-page-mode='${payload}']`);
        if (activeMode) {
            pageModeOptionsDom.querySelectorAll('li').forEach(li => li.classList.remove('active'));
            activeMode.classList.add('active');
        }
    };

// 内容变更处理函数
    const handleContentChange = async function () {
        // 字数
        const wordCount = await instance.command.getWordCount();
        const wordCountDom = document.querySelector('.word-count');
        if (wordCountDom) {
            wordCountDom.innerText = `${wordCount || 0}`;
        }

        // 目录
        if (isCatalogShow) {
            nextTick(() => {
                updateCatalog();
            });
        }

        // 批注
        nextTick(() => {
            updateComment();
        });
    };

// 内容变更监听，使用防抖函数
    instance.listener.contentChange = debounce(handleContentChange, 200);
    handleContentChange();

// 保存监听
    instance.listener.saved = function (payload) {
        console.log('elementList: ', payload);
    };

// 右键菜单注册
    instance.register.contextMenuList([
      {
        name:'单元格宽度',
        when: (payload) => {
          return payload.options.mode === 'edit' && payload.isInTable;
        },
        childMenus:[
          {
            name:'三列单元格宽度',
            when: (payload) => {
              return payload.options.mode === 'edit' && payload.isInTable;
            },
            callback: (command) => {
              const context = command.getRangeContext();

              if (context.tableElement.colgroup.length === 3) {
                command.executeUpdateElementById({
                  id: context.tableElement.id,
                  properties:{
                    colgroup:[
                      {
                        width: 265,
                      },
                      {
                        width: 290,
                      },
                      {
                        width: 150,
                      }
                    ]
                  }
                })
              }
            }
          },
          {
            name:'六列单元格宽度',
            when: (payload) => {
              return payload.options.mode === 'edit' && payload.isInTable;
            },
            callback: (command) => {
              const context = command.getRangeContext();

              if (context.tableElement.colgroup.length === 6) {
                command.executeUpdateElementById({
                  id: context.tableElement.id,
                  properties:{
                    colgroup:[
                      {
                        width: 50,
                      },
                      {
                        width: 135,
                      },
                      {
                        width: 305,
                      },
                      {
                        width: 45,
                      },
                      {
                        width: 80,
                      },
                      {
                        width: 80,
                      },
                    ]
                  }
                })
              }
            }
          }
        ],

      },
      {
        isDivider: true,
      },
      {
        name: '字体',
        when: (payload) => {
          return payload.options.mode === 'edit' && payload.editorHasSelection || payload.isInTable;
        },
        childMenus:[
          {
            name: '宋体',
            when: (payload) => {
              return payload.options.mode === 'edit' && payload.editorHasSelection || payload.isInTable;
            },
            callback: (command) => {
              command.executeFont('宋体');
            }
          },
          {
            name: 'Times New Roman',
            when: (payload) => {
              return payload.options.mode === 'edit' && payload.editorHasSelection || payload.isInTable;
            },
            callback: (command) => {
              command.executeFont('Times New Roman');
            }
          },
          {
            name: '黑体',
            when: (payload) => {
              return payload.options.mode === 'edit' && payload.editorHasSelection || payload.isInTable;
            },
            callback: (command) => {
              command.executeFont('黑体');
            }
          }
        ],
      },
      {
        name: '字号',
        when: (payload) => {
          return payload.options.mode === 'edit' && payload.editorHasSelection || payload.isInTable;
        },
        childMenus:[
          {
            name: '四号',
            when: (payload) => {
              return payload.options.mode === 'edit' && payload.editorHasSelection || payload.isInTable;
            },
            callback: (command) => {
              command.executeSize('18');
            }
          },
          {
            name: '小四',
            when: (payload) => {
              return payload.options.mode === 'edit' && payload.editorHasSelection || payload.isInTable;
            },
            callback: (command) => {
              command.executeSize('16');
            }
          },
          {
            name: '小五',
            when: (payload) => {
              return payload.options.mode === 'edit' && payload.editorHasSelection || payload.isInTable;
            },
            callback: (command) => {
              command.executeSize('14');
            }
          },
        ],
      },
    ]);

// 快捷键注册
    instance.register.shortcutList([
        {
            key: 'P',
            mod: true,
            isGlobal: true,
            callback: (command) => {
                command.executePrint();
            }
        },
        {
            key: 'F',
            mod: true,
            isGlobal: true,
            callback: (command) => {
                const text = command.getRangeText();
                searchDom.click();
                if (text) {
                    searchInputDom.value = text;
                    instance.command.executeSearch(text);
                    setSearchResult();
                }
            }
        },
        {
            key: 'Minus',
            ctrl: true,
            isGlobal: true,
            callback: (command) => {
                command.executePageScaleMinus();
            }
        },
        {
            key: 'Equal',
            ctrl: true,
            isGlobal: true,
            callback: (command) => {
                command.executePageScaleAdd();
            }
        },
        {
            key: 'Zero',
            ctrl: true,
            isGlobal: true,
            callback: (command) => {
                command.executePageScaleRecovery();
            }
        }
    ]);

    return {instance};
}