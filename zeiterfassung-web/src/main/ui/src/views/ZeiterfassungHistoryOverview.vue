<template>
  <div>
    <h2 class="centeredText">{{ title }}</h2>
    <div>
      <column-chart
        empty="Keine Daten"
        loading="Lade Daten..."
        xtitle="Tage" ytitle="Stunden"
        suffix="h"
        :dataset="{borderColor: '#004587', backgroundColor: '#358fe6'}"
        :data="businessDayHistoryChartData"
      >
      </column-chart>
    </div>
  </div>
</template>

<script>
import businessDayHistoryApi from '../mixins/BusinessDayHistoryApi';

export default {
  name: 'BusinessDayHistoryChart',
  mixins: [businessDayHistoryApi],
  computed: {
    businessDayHistoryChartData() {
      return this.$store.getters.businessDayHistoryChartData;
    }
  },
  data() {
    return {
      title: 'Monatsübersicht Buchungen',
    }
  },
  mounted() {
    this.fetchBusinessDayHistoryChartData();
  }
}
</script>
