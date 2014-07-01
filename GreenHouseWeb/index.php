<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>VUB Greenhouse Optimisation</title>
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
        <script src="js/highcharts.js"></script>
        <script src="js/modules/exporting.js"></script>

        <?php
        $valid = isset($_POST['opt_temp']);
        if ($valid) {
            $postData = array(
                "opt_temp" => $_POST["opt_temp"],
                "opt_bright" => $_POST["opt_bright"],
                "wT" => $_POST["weight_temp"],
                "wB" => $_POST["weight_bright"],
                "budget" => $_POST["budget"],
                "max_temp_heat" => $_POST["temp_heat"],
                "max_bright_light" => $_POST["max_bright"],
            );

            $wsdl = "http://localhost:8080/OptimizeGreenhouseWS/GreenhouseOptWS?wsdl";
            $client = new SoapClient($wsdl);

            $response = $client->getCostEfficiency(array("json" => json_encode($postData)));
        }
        ?>

        <script type="text/javascript">
            $(function() {
                if (<?php echo $valid; ?>) {

                    var obj = jQuery.parseJSON('<?php echo $response->return; ?>');
                    var minutes = [];
                    $.each(obj.t, function(i, item) {
                        minutes.push(Math.round(item));
                    });
                    var eff_temp = [];
                    $.each(obj.T_efficiency, function(i, item) {
                        eff_temp.push(Math.log(item));
                    });
                    var eff_bright = [];
                    $.each(obj.B_efficiency, function(i, item) {
                        eff_bright.push(Math.log(item));
                    });
                    var aggr_temp = [];
                    var aggr_brigh = [];
                    var aux_temp = 0;
                    var aux_brigh = 0;

                    for (i = 0; i < 5; i++) {
                        aux_temp = 0;
                        aux_brigh = 0;
                        for (j = Math.floor(i * minutes.length / 5); j < Math.floor((i + 1) * minutes.length / 5); j++) {
                            aux_temp += obj.temp_cost[j];
                            aux_brigh += obj.B_cost[j];
                        }
                        aggr_temp.push(aux_temp);
                        aggr_brigh.push(aux_brigh);
                    }


                    $('#container_temp').highcharts({
                        title: {
                            text: 'Temperature - Time',
                            x: -20 //center
                        },
                        xAxis: {
                            title: {
                                text: 'Time (h)'
                            },
                            tickInterval: 60,
                            categories: minutes
                        },
                        yAxis: {
                            title: {
                                text: 'Temperature (°C)'
                            },
                            plotLines: [{
                                    value: 0,
                                    width: 1,
                                    color: '#808080'
                                }]
                        },
                        tooltip: {
                            valueSuffix: '°C'
                        },
                        legend: {
                            layout: 'vertical',
                            align: 'right',
                            verticalAlign: 'middle',
                            borderWidth: 0
                        },
                        series: [{
                                name: 'Greenhouse temperature',
                                data: obj.temperature
                            },
                            {
                                name: 'Forecasted temperature',
                                data: obj.T_predicted
                            }]
                    });
                    $('#container_bright').highcharts({
                        title: {
                            text: 'Brightness - Time',
                            x: -20 //center
                        },
                        xAxis: {
                            title: {
                                text: 'Time (h)'
                            },
                            tickInterval: 60,
                            categories: minutes
                        },
                        yAxis: {
                            title: {
                                text: 'Brightness (lm)'
                            },
                            plotLines: [{
                                    value: 0,
                                    width: 1,
                                    color: '#808080'
                                }]
                        },
                        tooltip: {
                            valueSuffix: 'lm'
                        },
                        legend: {
                            layout: 'vertical',
                            align: 'right',
                            verticalAlign: 'middle',
                            borderWidth: 0
                        },
                        series: [{
                                name: 'Greenhouse brightness',
                                data: obj.B
                            },
                            {
                                name: 'Predicted brightness (cloudiness)',
                                data: obj.B_predicted
                            }]
                    });

                    $('#container_cost').highcharts({
                        title: {
                            text: 'Cost - Time',
                            x: -20 //center
                        },
                        xAxis: {
                            title: {
                                text: 'Time (h)'
                            },
                            tickInterval: 60,
                            categories: minutes
                        },
                        yAxis: {
                            title: {
                                text: 'Cost (kWh)'
                            },
                            plotLines: [{
                                    value: 0,
                                    width: 1,
                                    color: '#808080'
                                }]
                        },
                        tooltip: {
                            valueSuffix: 'kWh'
                        },
                        legend: {
                            layout: 'vertical',
                            align: 'right',
                            verticalAlign: 'middle',
                            borderWidth: 0
                        },
                        series: [{
                                name: 'Temperature Cost',
                                data: obj.temp_cost
                            }, {
                                name: 'Brightness Cost',
                                data: obj.B_cost
                            }]
                    });

                    $('#container_eff').highcharts({
                        title: {
                            text: 'Efficiency - Time',
                            x: -20 //center
                        },
                        xAxis: {
                            title: {
                                text: 'Time (h)'
                            },
                            tickInterval: 60,
                            categories: minutes
                        },
                        yAxis: {
                            title: {
                                text: 'Efficiency deviation'
                            },
                            plotLines: [{
                                    value: 0,
                                    width: 1,
                                    color: '#808080'
                                }]
                        },
                        legend: {
                            layout: 'vertical',
                            align: 'right',
                            verticalAlign: 'middle',
                            borderWidth: 0
                        },
                        series: [{
                                name: 'Temperature efficiency',
                                data: eff_temp
                            },
                            {
                                name: 'Brightness efficiency',
                                data: eff_bright
                            }]
                    });
                    $('#container_cost_aggr').highcharts({
                        chart: {
                            type: 'column'
                        },
                        title: {
                            text: 'Temperature and brightness cost contribution'
                        },
                        xAxis: {
                            categories: [
                                'Day 1',
                                'Day 2',
                                'Day 3',
                                'Day 4',
                                'Day 5',
                            ]
                        },
                        yAxis: {
                            min: 0,
                            title: {
                                text: 'Cost (kWh)'
                            }
                        },
                        tooltip: {
                            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                                    '<td style="padding:0"><b>{point.y:.1f} kWh</b></td></tr>',
                            footerFormat: '</table>',
                            shared: true,
                            useHTML: true
                        },
                        plotOptions: {
                            column: {
                                pointPadding: 0.2,
                                borderWidth: 0
                            }
                        },
                        series: [{
                                name: 'Temperature cost',
                                data: aggr_temp
                            }, {
                                name: 'Brightness cost',
                                data: aggr_brigh
                            }]
                    });

                    $("#summary").append("<table border=\"1\" >" +
                            "<tr><td>Temperature cost</td><td>" + Math.round(obj.total_T_cost * 100) / 100 + "</td></tr>" +
                            "<tr><td>Brightness cost</td><td>" + Math.round(obj.total_B_cost * 100) / 100 + "</td></tr>" +
                            "<tr><td>Total cost</td><td>" + Math.round(obj.total_cost * 100) / 100 + "</td></tr>" +
                            "<tr><td>Temperature efficiency</td><td>" + Math.round(obj.total_B_efficiency * 100) / 100 + "</td></tr>" +
                            "<tr><td>Brightness efficiency</td><td>" + Math.round(obj.total_T_efficiency * 100) / 100 + "</td></tr>" +
                            "<tr><td>Total efficiency</td><td>" + Math.round(obj.total_efficiency * 100) / 100 + "</td></tr>" +
                            "</table>");
                }
            });


        </script>
    </head>
    <body>
        <div id="form_container">

            <center>
                <h1>VUB Greenhouse Cost-Efficiency Optimisation</h1>
                <form action="index.php" method="post" autocomplete="on">
                    <table>
                        <tr><td>Optimal temperature (°C):</td><td><input type="text" name="opt_temp" value="22"></td></tr>
                        <tr><td>Optimal brightness (lm):</td><td><input type="text" name="opt_bright" value="1000"></td></tr>
                        <tr><td>Temperature weight:</td><td> <input type="text" name="weight_temp" value="0.7"></td></tr>
                        <tr><td>Brightness weight:</td><td> <input type="text" name="weight_bright" value="0.3"></td></tr>
                        <tr><td>Budget:</td><td> <input type="text" name="budget" value="350"></td></tr>
                        <tr><td>Maximum temperature change per hour: </td><td><input type="text" name="temp_heat" value="4"></td></tr>
                        <tr><td>Maximum brightness variation: </td><td><input type="text" name="max_bright" value="750"></td></tr>
                        <tr><td></td><td><input type="submit" value="Optimise"></td></tr>
                    </table>
                </form> 
            </center>
        </div>

        <div id="container_temp" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
        <div id="container_bright" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
        <div id="container_cost" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
        <div id="container_eff" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
        <div id="container_cost_aggr" style="min-width: 310px; height: 400px; margin: 0 auto"></div>       

        <?php
        if ($valid) {
            echo '<center> <h2>Summary</h2>            
                <div id="summary"></div>
                </center>
                <br>';
        }
        ?>

    </body>
</html>
