VIEWPORT_WIDTH = 500;
VIEWPORT_HEIGHT = 300;
GRID_WIDTH = 100;

SIZES = [100, 1000, 10000];

function initializeDatasets() {
  $.each(SIZES, function(i, size) {
    doSync({ size:size, width:VIEWPORT_WIDTH, height:VIEWPORT_HEIGHT }, function(resp) {
      if ( resp.error ) {
        alert('Dataset Creation Failed: ' + resp.error);
      } else {
        renderDataset('#cluster0', i, resp);
      }
    });
  });

  $.each(SIZES, function(i, size) {
    $.getJSON('/data', { size:size, width:VIEWPORT_WIDTH, height:VIEWPORT_HEIGHT, cluster:'grid-center' }, function(resp) {
      var x = d3.scale.linear()
           .domain([0, d3.max(resp, function(d) { return d.count; })])
           .range([0, Math.min(GRID_WIDTH/2, d3.mean(resp, function(d) { return d.count; }))]);

      renderDataset('#cluster1', i, resp, function(d) { return 3+x(d.count); });
    });
  });

  $.each(SIZES, function(i, size) {
    $.getJSON('/data', { size:size, width:VIEWPORT_WIDTH, height:VIEWPORT_HEIGHT, cluster:'grid-avg' }, function(resp) {
      var x = d3.scale.linear()
           .domain([0, d3.max(resp, function(d) { return d.count; })])
           .range([0, Math.min(GRID_WIDTH/2, d3.mean(resp, function(d) { return d.count; }))]);

      renderDataset('#cluster2', i, resp, function(d) { return 3+x(d.count); });
    });
  });

  $.each(SIZES, function(i, size) {
    $.getJSON('/data', { size:size, width:VIEWPORT_WIDTH, height:VIEWPORT_HEIGHT, cluster:'float', maxDistance:50 }, function(resp) {
      var x = d3.scale.linear()
           .domain([0, d3.max(resp, function(d) { return d.count; })])
           .range([0, Math.min(25, d3.mean(resp, function(d) { return d.count; }))]);

      renderDataset('#cluster3', i, resp, function(d) { return 3+x(d.count); });
    });

    $.getJSON('/data', { size:size, width:VIEWPORT_WIDTH, height:VIEWPORT_HEIGHT, cluster:'float', maxDistance:100 }, function(resp) {
      var x = d3.scale.linear()
           .domain([0, d3.max(resp, function(d) { return d.count; })])
           .range([0, Math.min(50, d3.mean(resp, function(d) { return d.count; }))]);

      renderDataset('#cluster4', i, resp, function(d) { return 3+x(d.count); });
    });

    $.getJSON('/data', { size:size, width:VIEWPORT_WIDTH, height:VIEWPORT_HEIGHT, cluster:'float', maxDistance:150 }, function(resp) {
      var x = d3.scale.linear()
           .domain([0, d3.max(resp, function(d) { return d.count; })])
           .range([0, Math.min(75, d3.mean(resp, function(d) { return d.count; }))]);

      renderDataset('#cluster5', i, resp, function(d) { return 3+x(d.count); });
    });
  });
}

function renderDataset(selector, idx, data, radius, nogrid) {
  var graph = d3.select(selector).append('svg')
        .style('left', (idx*(VIEWPORT_WIDTH+12)) + 'px')
        .attr('width', VIEWPORT_WIDTH)
        .attr('height', VIEWPORT_HEIGHT);

  graph.selectAll('circle')
        .data(data)
          .enter()
        .append('circle')
          .attr('r', radius || 3)
          .attr('cx', function(d) {return d.x })
          .attr('cy', function(d) {return d.y });

  if ( !nogrid ) {
    drawGridlines(graph);
  }
}

function drawGridlines(graph) {
  var i;
  for ( i = GRID_WIDTH; i < VIEWPORT_WIDTH; i += GRID_WIDTH ) {
    graph.append('line')
            .attr('x1', i)
            .attr('x2', i)
            .attr('y1', 0)
            .attr('y2', VIEWPORT_HEIGHT);
  }

  for ( i = GRID_WIDTH; i < VIEWPORT_HEIGHT; i+= GRID_WIDTH ) {
    graph.append('line')
            .attr('x1', 0)
            .attr('x2', VIEWPORT_WIDTH)
            .attr('y1', i)
            .attr('y2', i);
  }
}

function doSync(data, fn) {
  $.ajax({
    url:'/data',
    type:'get',
    dataType:'json',
    async:false,
    data:data,
    success:fn
  });
}