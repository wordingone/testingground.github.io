window.addEventListener('DOMContentLoaded', () => {
  const explore = document.querySelector('.Explore');
  const moodboard = document.querySelector('.InitialMoodboardFrame');
  const zoomButtons = document.querySelectorAll('.ZoomExtents');

  if (explore && moodboard) {
    explore.addEventListener('click', () => {
      moodboard.scrollIntoView({ behavior: 'smooth' });
    });
  }

  zoomButtons.forEach(btn => {
    btn.style.cursor = 'pointer';
    btn.addEventListener('click', () => {
      window.scrollTo({ top: 0, behavior: 'smooth' });
    });
  });
});
