<template>
<div>
  <h2 class="centeredText">{{ title }}</h2>
    <pie-chart
      class="centered"
      empty="Keine Daten"
      loading="Lade Daten..."
      id="set-actual-pie-chart"
      legend="bottom"
      width="230px" height="230px"
      suffix="h"
      donut="true"
      :round="2"
      :colors="['#004587', '#358fe6']"
      :data="setActualWorkingPieChartData"
    >
    </pie-chart>
    <label>Tagessoll: {{setHours}}h</label>
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
