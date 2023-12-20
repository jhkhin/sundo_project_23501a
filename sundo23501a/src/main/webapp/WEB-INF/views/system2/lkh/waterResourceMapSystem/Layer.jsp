<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>수자원 등록 </title>
    <script>
        <link rel="stylesheet"  href="http://localhost:8090/geoserver/openlayers3/ol.css"   type="text/css">
        <link rel="stylesheet" href="http://localhost:8090/geoserver/openlayers3/ol.css" type="text/css">
    </script>
    <script src="http://localhost:8090/geoserver/openlayers3/ol.js" type="text/javascript"></script>
    <script>
        $(function() {

            $.ajax({
                url			: '/main_header_2',
                dataType 	: 'html',
                success		: function(data) {
                    $('#header').html(data);
                }
            });

            $.ajax({
                url			: '/main_footer',
                dataType 	: 'html',
                success		: function(data) {
                    $('#footer').html(data);
                }
            });
        });

    </script>
    <style>

        header {
            height: 55px;
        }

        body {
            font-family: Verdana, Geneva, Arial, Helvetica, sans-serif;
            font-size: small;
        }

        iframe {
            width: 100%;
            height: 250px;
            border: none;
        }

        #map {
            clear: both;
            position: relative;
            width: 1003px;
            height: 600px;
            border: 1px solid black;
        }

        #wrapper {
            width: 1003px;
        }


        h1 {
            color: blue;
        }

        hr {
            border: solid 1px yellow;
        }

        .custom-mouse-position {
            color: blue;
            font-family: Arial;
            font-size: 10pt;
        }
    </style>
</head>
<body>
    <header id="header"></header>

    <div class="container">

        <div class="row">
            <div id="center">


                <div class="flex-shrink-0 p-3">
                    <ul class="list-unstyled ps-0">
                        <li class="mb-1">
                            <button class="btn btn-toggle d-inline-flex align-items-center rounded border-0 collapsed" data-bs-toggle="collapse" data-bs-target="#mypost-collapse" aria-expanded="true">
                                사용자 관리
                            </button>
                            <div class="collapse show" id="mypost-collapse">
                                <ul class="btn-toggle-nav list-unstyled fw-normal pb-1">
                                    <li><a href="/admin_userinfo_list" class="link-body-emphasis d-inline-flex text-decoration-none rounded">사용자 관리</a></li>
                                    <li><a href="/admin_org_list" class="link-body-emphasis d-inline-flex text-decoration-none rounded">기관 관리</a></li>
                                </ul>
                            </div>
                        </li>
                        <li class="mb-1">
                            <button class="btn btn-toggle d-inline-flex align-items-center rounded border-0 collapsed" data-bs-toggle="collapse" data-bs-target="#mypost-collapse" aria-expanded="true">
                                운영 관리
                            </button>
                            <div class="collapse show" id="mypost-collapse">
                                <ul class="btn-toggle-nav list-unstyled fw-normal pb-1">
                                    <li><a href="/admin_notice_list" class="link-body-emphasis d-inline-flex text-decoration-none rounded">공지사항</a></li>
                                </ul>
                            </div>
                        </li>
                        <li class="mb-1">
                            <button class="btn btn-toggle d-inline-flex align-items-center rounded border-0 collapsed" data-bs-toggle="collapse" data-bs-target="#mypost-collapse" aria-expanded="true">
                                시스템 관리
                            </button>
                            <div class="collapse show" id="mypost-collapse">
                                <ul class="btn-toggle-nav list-unstyled fw-normal pb-1">
                                    <li><a href="/system_log" class="link-body-emphasis d-inline-flex text-decoration-none rounded">접속 통계</a></li>
                                    <li><a href="/access_log" class="link-body-emphasis d-inline-flex text-decoration-none rounded">접속 이력</a></li>
                                    <li><a href="/login_log" class="link-body-emphasis d-inline-flex text-decoration-none rounded">로그인 이력</a></li>
                                </ul>
                            </div>
                        </li>

                    </ul>
                </div>


                <h2>실시간 수문정보 관리 시스템</h2>
                <h3>레이어 부분</h3>

                <div style="border: solid 1px black; margin: 3%">
                    <button id="activateLayerButton">서울 한강 </button>
                    <button id="sumun">경기 한강 </button>
                    <button id="sumun2">수문위치 </button>
                    <button id="sumun3"> 관측소 위치</button>
                </div>

                <div style="border: solid 1px black; margin: 3%">

                    <button id="dam">댐 </button>
                    <button id="waterway_pos">관개수로 </button>
                    <button id="pump"> 펌프장</button>
                </div>
                <div id="map">

                </div>
                <form>
                    <label>Projection </label> <select id="projection">
                    <option value="EPSG:4326">EPSG:4326</option>
                    <option value="EPSG:900913">EPSG:900913</option>
                    <option value="EPSG:3857">EPSG:3857</option>
                </select> <label>Precision </label> <input id="precision" type="number" min="0"
                                                           max="12" value="4" />
                    <div id = "mouse-position"></div>
                </form>
                <div id="wrapper">
                    <div id="location"></div>


                </div>
            </div>
        </div>
    </div>


    <footer class="footer py-2">
        <div id="footer" class="container">
        </div>
    </footer>

    <script type="text/javascript">
        var map = new ol.Map({
            target: 'map',
            layers: [
                new ol.layer.Tile({
                    source: new ol.source.OSM()
                }),
                new ol.layer.Image({
                    source: new ol.source.ImageWMS({
                        url: 'http://localhost:8090/geoserver/wms',
                        params: {
                            'LAYERS': 'lee:SEOUL_HANGANG_HACUN',
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
                            'LAYERS':'lee:sumun_pos',
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
                            'LAYERS':'lee:observation_pos',
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
                }),
                new ol.layer.Image({
                    source: new ol.source.ImageWMS({
                        url: 'http://localhost:8090/geoserver/wms',
                        params: {
                            'LAYERS':'lee:waterway_pos',
                            'TILED': true
                        },
                        serverType: 'geoserver'
                    }),
                    visible: false // 초기에는 레이어를 숨겨둠
                })
            ],
            view: new ol.View({
                center: ol.proj.fromLonLat([126.9780, 37.5665]), // 서울 시청 좌표
                zoom: 10
            })
        });

        $('#activateLayerButton').on('click', function () {
            toggleLayerVisibility(1); // 두 번째 레이어의 가시성 토글
        });

        $('#sumun').on('click', function () {
            toggleLayerVisibility(2); // 세 번째 레이어의 가시성 토글
        });

        $('#sumun2').on('click', function () {
            toggleLayerVisibility(3); // 세 번째 레이어의 가시성 토글
        });
        $('#sumun3').on('click', function () {
            toggleLayerVisibility(4); // 세 번째 레이어의 가시성 토글
        });
        $('#dam').on('click', function () {
            toggleLayerVisibility(5); // 세 번째 레이어의 가시성 토글
        });
        $('#waterway_pos').on('click', function () {
            toggleLayerVisibility(6); // 세 번째 레이어의 가시성 토글
        });

        function toggleLayerVisibility(layerIndex) {
            var layer = map.getLayers().getArray()[layerIndex];
            layer.setVisible(!layer.getVisible());
        }
    </script>
</body>
</html>



