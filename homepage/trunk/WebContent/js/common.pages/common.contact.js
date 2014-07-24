	function checkContactTag(){
		$('div[data-address-tag]').each(function(){
			var that = $(this);				
			//data-address-tag='인키움' data-search-type='1'  data-show-type='table'
			var srchName = that.attr('data-address-tag');
			var srchType = that.attr('data-search-type');
			var showType = that.attr('data-show-type'); 
			var contact = new common.models.Contact();
			contact.set("tag", srchName);
			contact.set("srchType", srchType);
			common.api.callback({  
				url : '/community/html-contacts-tags.do?output=json',
				data : { item: kendo.stringify(contact) },
				success : function(response){
					//var list = response.contactsByTagNames;
					var htmlCode = response.htmlCodeContactsByTagNames;
					//console.log(htmlCode);
					that.html(htmlCode);
					//console.log(htmlCode);
					/*if(showType == 'table'){
						makeContactTable(list, that);
					}else{
						console.log('showType 에 맞는 출력 컨트롤이 없습니다...');
					}
					*/
					// 렌더링 된 후 스타일을 바꿔본다. 
					// that
					//that.css();
				}
			});
		});
		
	}
	
	function makeContactTable(list, dom){
		console.log('makeContactTable');
		var renderTo = dom;
		var str = '';
		var maxDepth = 3; //변경 가능..
		if(list.length == 0){
			str = '검색된 연락처가 없습니다.';
		}else{
			str = "<table class='table table-bordered k-table'><colgroup></colgroup><tr style='background:#f5f5f5;'><th colspan='"+maxDepth+"'>상품 및 서비스</th><th>담당자</th><th>연락처</th></tr>";
			for(var i=0; i< list.length; i++){                                                                                                                                                                                                                                                                                                         
				str += "<tr>";
				// SPAN 영역
				var colspan = 1;
				for(var j=0; j < maxDepth; j++){
					if(list[i].groupNames[j] == list[i].groupNames[j+1]){
						colspan++;
					}else{
						if(colspan > 1){
							str += "<td colspan='"+colspan+"'>" + list[i].groupNames[j] +'</td>';
						}else{
							str += "<td>" + list[i].groupNames[j] +'</td>';
						}
						colspan = 1;
					}
				}
				// SPAN 영역
				str += '<td>' + list[i].name + '</td>';
				str += "<td><a href='tel:+" + list[i].phone + "'><i class='fa fa-phone'></i>" + list[i].phone + "</a> &nbsp;";
				str += "<a href='mailto:" + list[i].email + "'><i class='fa fa-envelope'></i>" + list[i].email + "</a></td>";
				str += '</tr>';
				
			}
			str += '</table>';
		}
		console.log(str);
		renderTo.html(str);
		//console.log('renderTo.html() : ' + renderTo.html());
		//renderTo.text(str);
	}
