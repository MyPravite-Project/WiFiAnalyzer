/*
 *    Copyright (C) 2015 - 2016 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.vrem.wifianalyzer.wifi.graph;

import android.support.annotation.NonNull;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.vrem.wifianalyzer.MainContext;
import com.vrem.wifianalyzer.wifi.model.WiFiBand;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

class GraphViewUtils {
    private final MainContext mainContext = MainContext.INSTANCE;
    private final GraphView graphView;
    private final Map<String, LineGraphSeries<DataPoint>> seriesMap;
    private GraphLegend graphLegend;

    public GraphViewUtils(@NonNull GraphView graphView, @NonNull Map<String, LineGraphSeries<DataPoint>> seriesMap) {
        this.graphView = graphView;
        this.seriesMap = seriesMap;
        this.graphLegend = mainContext.getSettings().getGraphLegend();
    }

    void updateSeries(@NonNull Set<String> newSeries) {
        List<String> remove = new ArrayList<>();
        for (String title : seriesMap.keySet()) {
            if (!newSeries.contains(title)) {
                graphView.removeSeries(seriesMap.get(title));
                remove.add(title);
            }
        }
        for (String title : remove) {
            seriesMap.remove(title);
        }
    }

    void updateLegend() {
        resetLegendRenderer();
        LegendRenderer legendRenderer = graphView.getLegendRenderer();
        legendRenderer.setVisible(isVisible());
        legendRenderer.resetStyles();
        legendRenderer.setWidth(0);
        positionLegend(legendRenderer);
        legendRenderer.setTextSize(legendRenderer.getTextSize() * 0.50f);
    }

    private void positionLegend(LegendRenderer legendRenderer) {
        if (GraphLegend.RIGHT.equals(graphLegend)) {
            legendRenderer.setAlign(LegendRenderer.LegendAlign.TOP);
        } else if (GraphLegend.LEFT.equals(graphLegend)) {
            legendRenderer.setFixedPosition(0, 0);
        }
    }

    private boolean isVisible() {
        return !GraphLegend.HIDE.equals(this.graphLegend);
    }

    private void resetLegendRenderer() {
        if (!graphLegend.equals(mainContext.getSettings().getGraphLegend())) {
            LegendRenderer legendRenderer = new LegendRenderer(graphView);
            graphView.setLegendRenderer(legendRenderer);
            graphLegend = mainContext.getSettings().getGraphLegend();
        }
    }

    void setVisibility(@NonNull WiFiBand wiFiBand) {
        graphView.setVisibility(wiFiBand.equals(mainContext.getSettings().getWiFiBand()) ? View.VISIBLE : View.GONE);
    }

}
