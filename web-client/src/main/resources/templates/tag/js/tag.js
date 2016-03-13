	function saveTag() {
		if (operate == "add") {
			insertRow(getDlgRow());
		} else {
			updateRow(getDlgRow());
		}
		$('#dlg').dialog('close');
	}

	function saveCategory() {
		$('#category-dlg').dialog('close');
	}

	function onClickCell(index, field, value) {
		if ('update' == field) {
			updateTag(index);
		}
		if ('delete' == field) {
			deleteRow(index);
		}
	}

	function showCtyDlg() {
		$("dlgCategoryName").textbox('setValue', '');
		$('#category-dlg').dialog('open').dialog('center').dialog('setTitle',
				'修改兴趣点');
	}

	function updateTag(index) {
		showDeatilDlg(index);

	}
	var operate = "add";
	function showDeatilDlg(index) {
		operate = "add";
		if (index || index == '0') {
			operate = "edit";
			var rows = $('#tagTable').datagrid('getRows');
			var row = rows[index];
			$("#tagName").textbox('setValue', row.tagName);
			$("#tagCategory").combobox('setValue', row.categoryName);
			$("#rowIndex").val(index);
			$('#dlg').dialog('open').dialog('center').dialog('setTitle',
					'修改兴趣点');
		} else {
			$("#rowIndex").val('')
			$("#tagName").textbox('setValue', '');
			$("#tagCategory").combobox('setValue', '');
			$('#dlg').dialog('open').dialog('center').dialog('setTitle',
					'新增兴趣点');
		}

	}

	function getAddOrEdit() {
		return operate;
	}

	function getDlgRow() {
		var tagName = $("#tagName").textbox('getValue');
		var tagCategory = $("#tagCategory").textbox('getValue');
		var row = {
			index : $("#rowIndex").val(),
			tagName : tagName,
			categoryName : tagCategory
		}
		return row;
	}

	function updateRow(row) {
		$('#tagTable').datagrid('updateRow', {
			index : row.index,
			row : {
				tagName : row.tagName,
				categoryName : row.categoryName
			}
		});
	}

	function insertRow(row) {
		$('#tagTable').datagrid('appendRow', {
			tagName : row.tagName,
			categoryName : row.categoryName,
			update : '',
			del : ''
		});
	}

	function deleteRow(index) {
		$('#tagTable').datagrid('deleteRow', index)
	}
	function fmtUpdateIcon(value, row, index) {
		return "<span  style='cursor: pointer;font-size:24px;' class=\"icon-edit\" ;\">&nbsp;&nbsp;&nbsp;</span>";
	}
	function fmtDelIcon(value, row, index) {
		return "<span  style='cursor: pointer;font-size:24px;' class=\"icon-cancel\";\">&nbsp;&nbsp;&nbsp;</span>";
	}