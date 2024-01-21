<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>실시간 수문정보 관리시스템  </title>
    <link rel="stylesheet" type="text/css" href="/css/map.css">
    <script type="text/javascript" src="/js/map.js"></script>
    <script src="http://localhost:8090/geoserver/openlayers3/ol.js" type="text/javascript"></script>
    <link rel="stylesheet"  href="http://localhost:8090/geoserver/openlayers3/ol.css"   type="text/css">
    <style>
        .ol-popup {
            position: absolute;
            background-color: white;
            box-shadow: 0 1px 4px rgba(0,0,0,0.2);
            padding: 15px;
            border-radius: 10px;
            border: 1px solid #cccccc;
            bottom: 12px;
            left: -50px;
            min-width: 280px;
        }
        .ol-popup:after, .ol-popup:before {
            top: 100%;
            border: solid transparent;
            content: " ";
            height: 0;
            width: 0;
            position: absolute;
            pointer-events: none;
        }
        .ol-popup:after {
            border-top-color: white;
            border-width: 10px;
            left: 48px;
            margin-left: -10px;
        }
        .ol-popup:before {
            border-top-color: #cccccc;
            border-width: 11px;
            left: 48px;
            margin-left: -11px;
        }
        .ol-popup-closer {
            text-decoration: none;
            position: absolute;
            top: 2px;
            right: 8px;
        }
        .ol-popup-closer:after {
            content: "✖";
        }
    </style>

    <script>
        let view;
        let init_pos;
        let map;
        let datatype ="";
        let getdatatype ="";
        let standdatatype ="";
        $(function() {


            $.ajax({
                url         : '/main_header_2',
                dataType    : 'html',
                success      : function(data) {
                    $('#header').html(data);
                }
            });

            $.ajax({
                url         : '/main_footer',
                dataType    : 'html',
                success      : function(data) {
                    $('#footer').html(data);
                }
            });

            init_pos = ol.proj.fromLonLat([126.9780, 37.5665])
            view = new ol.View({
                center:init_pos,
                zoom:10
            })

            map = new ol.Map({
                target: 'map',
                layers: [
                    new ol.layer.Tile({
                        source: new ol.source.OSM()
                    }),
                    new ol.layer.Image({
                        source: new ol.source.ImageWMS({
                            url: 'http://localhost:8090/geoserver/wms',
                            params: {
                                //'LAYERS': 'lee:SEOUL_HANGANG_HACUN',
                                'LAYERS':'sundo:SEOUL_HANGANG_HACHUN',
                                'TILED': true
                            },
                            serverType: 'geoserver'
                        }),
                        visible: false // 초기에는 레이어를 숨겨둠
                    }),

                    new ol.layer.Image({
                        source: new ol.source.ImageWMS({
                            url: 'http://localhost:8090/geoserver/wms',
                            params: {
                                'LAYERS':'lee:GYEONGGI_HANGANG_HACUN',
                                
                                'TILED': true
                            },
                            serverType: 'geoserver'
                        }),
                        visible: false // 초기에는 레이어를 숨겨둠
                    }),

                    new ol.layer.Image({
                        source: new ol.source.ImageWMS({
                            url: 'http://localhost:8090/geoserver/wms',
                            params: {
                                'LAYERS':'lee:SEOUL_HANGANG_SUGYE',
                                'TILED': true
                            },
                            serverType: 'geoserver'
                        }),
                        visible: false // 초기에는 레이어를 숨겨둠
                    }),

                    new ol.layer.Image({
                        source: new ol.source.ImageWMS({
                            url: 'http://localhost:8090/geoserver/wms',
                            params: {
                                'LAYERS':'lee:GYEONGGI_HANGANG_SUGYE',
                                'TILED': true
                            },
                            serverType: 'geoserver'
                        }),
                        visible: false // 초기에는 레이어를 숨겨둠
                    }),

                    new ol.layer.Image({
                        source: new ol.source.ImageWMS({
                            url: 'http://localhost:8090/geoserver/wms',
                            params: {
                                'LAYERS':'sundo:SUMUN_POS',
                                'TILED': true
                            },
                            serverType: 'geoserver'
                        }),
                        visible: false // 초기에는 레이어를 숨겨둠
                    }),
                    new ol.layer.Image({
                        source: new ol.source.ImageWMS({
                            url: 'http://localhost:8090/geoserver/wms',
                            params: {
                                'LAYERS':'lee:OBSERVATION_POS',
                                'TILED': true
                            },
                            serverType: 'geoserver'
                        }),
                        visible: false // 초기에는 레이어를 숨겨둠
                    }),
                    new ol.layer.Image({
                        source: new ol.source.ImageWMS({
                            url: 'http://localhost:8090/geoserver/wms',
                            params: {
                                'LAYERS':'lee:dam_pos',
                                'TILED': true
                            },
                            serverType: 'geoserver'
                        }),
                        visible: false // 초기에는 레이어를 숨겨둠
                    })
                ],

                view: view,
            });
            // 전역 변수로 현재 열려 있는 팝업을 저장할 변수를 선언
            // let currentPopup = null;
            map.on('singleclick', function(evt) {
                var coordinate = evt.coordinate;
                console.log("되는거 ")
                console.log(coordinate)
                // var hdms = ol.coordinate.toStringHDMS(ol.proj.transform(coordinate, 'EPSG:3857', 'EPSG:4326'));

                let element = document.createElement("div");
                element.classList.add('ol-popup');
                element.innerHTML = coordinate;
                // 현재 열려 있는 팝업이 있으면 삭제
                if (currentPopup) {
                    map.removeOverlay(currentPopup);
                }
                // 새로운 OverLay 생성
                let overlay = new ol.Overlay({
                    element: element,
                    autoPan: true,
                    className: "multiPopup",
                    autoPanMargin: 100,
                    autoPanAnimation: {
                        duration: 400
                    }
                });
                // 오버레이의 위치 설정
                overlay.setPosition(coordinate);
                // 지도에 추가
                map.addOverlay(overlay);
                // 현재 열려 있는 팝업 갱신
                currentPopup = overlay;
                // 팝업 닫기 이벤트 처리
                element.addEventListener('click', function(e) {
                    var target = e.target;
                    if (target.className == "ol-popup-closer") {
                        // 선택한 OverLayer 삭제
                        map.removeOverlay(overlay);
                        // 현재 열려 있는 팝업 변수 초기화
                        currentPopup = null;
                    }
                });
            });

            $('#zoomOut').on('click',function (){
                var view = map.getView();
                var currentZoom = view.getZoom();
                view.setZoom(Math.max(currentZoom - 1, 0));
            })
            $('#zoomIn').on('click',function (){
                var view = map.getView();
                var currentZoom = view.getZoom();
                view.setZoom(currentZoom + 1);
            });

            $('#btn').on('click',function (){
                map.getView().setCenter(ol.proj.fromLonLat([126.9780, 37.5665]));
            });

        });


        function toggleLayerVisibility(layerIndex) {
            var layer = map.getLayers().getArray()[layerIndex];
            layer.setVisible(!layer.getVisible());
        }

        function layerClick(currentPage, mapping){
            console.log(currentPage);
            const ov = {"currentPage": currentPage}
            $.ajax({
                url: mapping,
                data: ov,
                success: function (data) {
                    getdatatype = data.type;
                    console.log("server get type: " + getdatatype);
                    /*관측소 요청*/
                    if (getdatatype == "Observation") {
                        toggleLayerVisibility(6);
                        console.log(data.objList);
                        showobsrv(getdatatype, data);   //  화면에 뿌리는 함수
                    }
                        /*관측소 요청*/

                    /*레이어요청*/
                    else if (getdatatype == "Layer") {
                        console.log("Layer");
                        showlayer(getdatatype, data);   //  화면에 뿌리는 함수
                    }
                        /*레이어요청*/
                    /*북마크요청*/
                    else if (getdatatype == "BookMark") {
                        console.log("BookMark");
                        showBookMark(getdatatype, data);   //  화면에 뿌리는 함수
                    }
                    /*북마크요청*/
                }
            })
        }

        let OPEN = false;
        let CON = false;

        //  좌측 레이어 클릭 시 팝업  ON / OFF
        function rightlayerShow(getdatatype) {
            var rightLayer = document.getElementById('right_layer');
            if (!rightLayer) {
                console.error("Element 'right_layer' not found.");
                return;
            }

            if(datatype != getdatatype || getdatatype == standdatatype){
                console.log("elel");
                console.log(standdatatype);
                console.log(datatype);

                datatype = getdatatype;
                rightLayer.style.display = 'block';
                $('#layer_top').empty();
                $('#layer_mid').empty();
                $('#layer_mid2').empty();
                $('#layer_mid3').empty();
                $('#layer_bot').empty();
                $('#list').empty();
            }
            else if (datatype == getdatatype) {
                datatype = getdatatype;
                rightLayer.style.display = 'none';
                standdatatype = getdatatype;
                console.log("standdatatype");
                console.log(standdatatype);
                console.log(datatype);
            }
        }

        function showBookMark(getdatatype, data) {
            rightlayerShow(getdatatype);
            let list = $('#list');
            const newcon = $('<div></div>');
            const newp = $('<p></p>');
            newp.append(data.type);
            newcon.append(newp);
            list.append(newcon);
        }

        function createDivCheckBox(divClassName, checkboxId, checkboxLabel, layerLevel,color='',call = true) {
            var newDiv = $('<div>', { class: divClassName });


            var checkBox = $('<input>', {
                type: 'checkbox',
                id: checkboxId,
                class: 'checkBox',
                click: function () {
                    if(call == true){
                        toggleLayerVisibility(layerLevel);
                    }
                    else{

                        call_observation();
                    }

                },
            });

            newDiv.append(checkBox);
            if(color != ''){
                var legend_box = $('<div>', {class: 'legend_box'});
                legend_box.css('background-color',color);
                newDiv.append(legend_box);
            }
            newDiv.append(checkboxLabel);

            return newDiv;
        }



        function showlayer(getdatatype, data) {
            rightlayerShow(getdatatype);
            // 새로운 div 요소 생성
            var layerMid = $('#layer_mid3');

            layerMid.append('<div class="list_level_1">하천도</div>');

            seoulHangang = createDivCheckBox("list_level_2","seoulHacun","서울한강",1,'#0a53be');
            gyeonggiHacun = createDivCheckBox("list_level_2","gyeonggiHacun","경기도한강",2,'#0a53be');
            seoulHacunSugye = createDivCheckBox("list_level_2","seoulHacunSugye","서울 한강 수계",3,'#55B4D1');
            gyeonggiHacunSugye = createDivCheckBox("list_level_2","gyeonggiHacunSugye","경기 한강 수계",4,'#55B4D1');
            sumun = createDivCheckBox("list_level_1","sumun","수문",5,'green');
            observation = createDivCheckBox("list_level_1","observation","관측소",6,'red');
            dam = createDivCheckBox("list_level_1","dam","댐",6,'red',false);
            layerMid.append(seoulHangang);
            layerMid.append(gyeonggiHacun);
            layerMid.append(seoulHacunSugye);
            layerMid.append(gyeonggiHacunSugye);
            layerMid.append(sumun);
            layerMid.append(observation);

        }

        function showobsrv(getdatatype, data) {
            console.log(data);
            rightlayerShow(getdatatype);

            var layerMid = $('#layer_mid');

            layerMid.append('<div>관측소</div>');
            layerMid.append('<input type="button" class="btn btn-dark" id = "total" value="'+ data.obj.total+' ">');

            var layerMid = $('#layer_mid2');
            layerMid.append('<div>관측소명</div>');

            var layerTop = document.getElementById('layer_top');
            if (!layerTop) {
                console.error("Element 'layer_top' not found.");
                return;
            }
            layerTop.innerHTML = '';

            var selectElement = document.createElement('select');
            var optionElement = document.createElement('option');

            optionElement.text = '전체';
            selectElement.classList.add('form-select');
            selectElement.style.width = 'auto';
            selectElement.appendChild(optionElement);
            layerTop.appendChild(selectElement);

            var inputElement = document.createElement('input');
            inputElement.setAttribute('placeholder', '관측소명을 입력하세요');
            inputElement.classList.add('form-control');
            layerTop.appendChild(inputElement);

            var searchButton = document.createElement('input');
            searchButton.setAttribute('type', 'button');
            searchButton.setAttribute('value', '검색');
            searchButton.setAttribute('id', 'search');
            searchButton.classList.add('btn', 'btn-dark');

            layerTop.appendChild(searchButton);

            const objList = data.objList;

            let list = $('#list');

            list.empty();
            $.each(objList, function (key, values) {
                const newcon = $('<div></div>');
                newcon.click(function () {
                    getLatLong(values.observe_code);
                });
                newcon.append(values.observe_post);
                newcon.append('<input type="hidden" value="' + values.latitude + '" id="lat' + values.observe_code + '">');
                newcon.append('<input type="hidden" value="' + values.longitude + '" id="long' + values.observe_code + '">');
                list.append(newcon);
            });
            var layerBot = document.getElementById('layer_bot');
            var paginationDiv = document.createElement('div');
            paginationDiv.setAttribute('id', 'paging');
            layerBot.appendChild(paginationDiv);
            var jspPagination = '';
            let obj = data.obj;
            var newPath = 'obsrvlist'; // 적절한 경로로 변경
            if (obj.startPage > obj.pageBlock) {
                jspPagination += '<div id="page-link" onclick="layerClick(' + (obj.startPage - obj.pageBlock) + ', \'' + newPath + '\')">이전</div>';
            }
            for (var i = obj.startPage; i <= obj.endPage; i++) {
                jspPagination += '<div id="page-item" onclick="layerClick(' + i + ', \'' + newPath + '\')"><div class="page-link">' + i + '</div></div>';
            }
            if (obj.endPage >= obj.pageBlock) {
                jspPagination += '<div id="page-link" onclick="layerClick(' + (obj.startPage + obj.pageBlock) + ', \'' + newPath + '\')">다음</div>';
            }
            jspPagination += '</div>';
            paginationDiv.innerHTML = jspPagination; // 페이징 코드 추가
        }


        function getLatLong(observe_code){  //  좌표 반환
            console.log(observe_code);
            let latvalue = $('#lat' + observe_code).val();
            let longvalue = $('#long' + observe_code).val();
            // console.log(latvalue);
            // console.log(longvalue);
            alert("latvalue"+latvalue +"longvalue"+ longvalue);
            const coordinates = [parseFloat(longvalue), parseFloat(latvalue)];  // 좌표를 배열로 변환
            moveToCoordinates(coordinates,10)
        }

        let currentPopup = null;
        function moveToCoordinates(coordinate, zoom) {     //  화면이동
            console.log("안되는거");
            console.log(coordinate);
            var view = map.getView();
            view.setCenter(ol.proj.fromLonLat(coordinate));
            var currentZoom = view.getZoom();
            view.setZoom(14);


            let element = document.createElement("div");
            element.classList.add('ol-popup');
            // 좌표값을 읽기 쉬운 형식으로 변환
            const coordinatesText = `Longitude: ${coordinate[0]}, Latitude: ${coordinate[1]}`;
            element.innerHTML = coordinatesText;
            // element.innerHTML = coordinate;
            // 현재 열려 있는 팝업이 있으면 삭제
            if (currentPopup) {
                map.removeOverlay(currentPopup);
            }
            // 새로운 OverLay 생성
            let overlay = new ol.Overlay({
                element: element,
                autoPan: true,
                className: "multiPopup",
                autoPanMargin: 100,
                autoPanAnimation: {
                    duration: 400
                }
            });
            // 오버레이의 위치 설정
            overlay.setPosition(coordinate);
            // 지도에 추가
            map.addOverlay(overlay);
            // 현재 열려 있는 팝업 갱신
            currentPopup = overlay;
            // 팝업 닫기 이벤트 처리
            element.addEventListener('click', function (e) {
                var target = e.target;
                if (target.className == "ol-popup-closer") {
                    // 선택한 OverLayer 삭제
                    map.removeOverlay(overlay);
                    // 현재 열려 있는 팝업 변수 초기화
                    currentPopup = null;
                }
            });
        }
    </script>
</head>
<body style="height: 84%">
<header id="header"></header>

<%--<div id="zoomButtons">--%>
<%--    <button id ="zoomIn">+</button>--%>
<%--    <button id ="zoomOut" >-</button>--%>
<%--</div>--%>
<div id="map">
    <div id="layer">
        <div id="left_layer">
            <div id="obsrv_box" onclick="layerClick(1 ,'obsrvlist')">
                <button type="button" class="btn btn-dark" style="width:100%;height:100%;border-radius:0px;">
	            	<svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" fill="currentColor" class="bi bi-thermometer-half" viewBox="0 0 16 16">
					  <path d="M9.5 12.5a1.5 1.5 0 1 1-2-1.415V6.5a.5.5 0 0 1 1 0v4.585a1.5 1.5 0 0 1 1 1.415"/>
					  <path d="M5.5 2.5a2.5 2.5 0 0 1 5 0v7.55a3.5 3.5 0 1 1-5 0zM8 1a1.5 1.5 0 0 0-1.5 1.5v7.987l-.167.15a2.5 2.5 0 1 0 3.333 0l-.166-.15V2.5A1.5 1.5 0 0 0 8 1"/>
					</svg>
					<p>관측소</p>
                </button>                
            </div>
            <div id="layer_box" onclick="layerClick(1,'layerlist')">
                <button type="button" class="btn btn-dark" style="width:100%;height:100%;border-radius:0px;">
	            	<svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" fill="currentColor" class="bi bi-layers-fill" viewBox="0 0 16 16">
					  <path d="M7.765 1.559a.5.5 0 0 1 .47 0l7.5 4a.5.5 0 0 1 0 .882l-7.5 4a.5.5 0 0 1-.47 0l-7.5-4a.5.5 0 0 1 0-.882l7.5-4z"/>
					  <path d="m2.125 8.567-1.86.992a.5.5 0 0 0 0 .882l7.5 4a.5.5 0 0 0 .47 0l7.5-4a.5.5 0 0 0 0-.882l-1.86-.992-5.17 2.756a1.5 1.5 0 0 1-1.41 0z"/>
					</svg>
					<p>레이어</p>
                </button>
            </div>
            <div id="bookmark_box" onclick="layerClick(1,'bookmarklist')">
                <button type="button" class="btn btn-dark" style="width:100%;height:100%;border-radius:0px;">
	            	<svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" fill="currentColor" class="bi bi-bookmark-fill" viewBox="0 0 16 16">
					  <path d="M2 2v13.5a.5.5 0 0 0 .74.439L8 13.069l5.26 2.87A.5.5 0 0 0 14 15.5V2a2 2 0 0 0-2-2H4a2 2 0 0 0-2 2"/>
					</svg>
					<p>북마크</p>
                </button>                
            </div>
        </div>
        
        <div id="right_layer" style="display: none">
            <div id="layer_top">

            </div>
            
            <div id="layer_mid">
                <div>관측소</div>
                <div id="total">total</div>
            </div>
            
            <div id="layer_mid2">
                <div>
                    관측소명
                </div>
                <div>
                    누적강우량
                </div>
            </div>
            
            <div id="layer_mid3">
                <div>
                    관측소명
                </div>
                <div>
                    누적강우량
                </div>
            </div>
            
            <div id="list">

            </div>
            
            <div id="layer_bot">
                <div id="paging" class="pagination"></div>

            </div>

        </div>

    </div>
</div>



<footer class="footer py-2">
    <div id="footer" class="container">
    </div>
</footer>

<script type="text/javascript">


</script>
</body>
</html>

