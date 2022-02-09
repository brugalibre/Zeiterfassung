<template>
  <div>
    <h2 class="centeredText">{{ title }}</h2>
    <pie-chart
      id="set-actual-pie-chart"
      :colors="['#004587', '#358fe6']"
      :data="setActualWorkingPieChartData"
      :round="2"
      class="centered"
      donut="true" empty="Keine Daten"
      height="230px"
      legend="bottom"
      loading="Lade Daten..."
      suffix="h"
      width="230px"
    >
    </pie-chart>
    <label>Tagessoll: {{ setHours }}h</label>
  </div>
</template>

<script>
import setActualWorkingPieApi from '../mixins/SetActualWorkingPieApi';

export default {
  name: 'SetActualComparison',
  mixins: [setActualWorkingPieApi],
  data() {
    return {
      title: 'Tagesübersicht',
    }
  },
  mounted() {
    this.fetchSetActualWorkingPieChartData();
  }
}
</script>
