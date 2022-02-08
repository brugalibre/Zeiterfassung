<template>
  <div>
    <h2 class="centeredText">{{ title }}</h2>
    <pie-chart
      id="ticketDistributionPieChart"
      :data="ticketDistributionChartData"
      :round="2"
      :colors="['#004587', '#358fe6', '#006bd1', '#2981d6', '#3b9fff', '#57adff']"
      class="centered"
      empty="Keine Daten"
      legend="bottom"
    >
    </pie-chart>
  </div>
</template>

<script>

export default {
  name: 'TicketDistribution',
  computed: {
    ticketDistributionChartData() {
      return this.$store.getters.ticketDistributionChartData;
    }
  },
  data() {
    return {
      title: 'Monatsübersicht Projekte',
    }
  }
}
</script>
