import os
import matplotlib.pyplot as plt
import matplotlib.dates as mdates
from datetime import datetime
import matplotlib
matplotlib.rcParams['font.family'] = 'Malgun Gothic'

# Sample data for issues
issues = [
    {"title": "Idea Confirmation", "start_date": datetime(2024, 11, 11), "end_date": datetime(2024, 11, 12)},
    {"title": "UI/DB constructing", "start_date": datetime(2024, 11, 12), "end_date": datetime(2024, 11, 14)},
    {"title": "Data Collecting", "start_date": datetime(2024, 11, 15), "end_date": datetime(2024, 11, 17)},
    {"title": "Prototype constructing", "start_date": datetime(2024, 11, 15), "end_date": datetime(2024, 11, 17)},
    {"title": "BackEnd Developing", "start_date": datetime(2024, 11, 15), "end_date": datetime(2024, 11, 24)},
    {"title": "FrontEnd Developing", "start_date": datetime(2024, 11, 15), "end_date": datetime(2024, 11, 24)},
    {"title": "Internal Check", "start_date": datetime(2024, 11, 20), "end_date": datetime(2024, 11, 21)},
    {"title": "Final Features Check", "start_date": datetime(2024, 11, 25), "end_date": datetime(2024, 11, 26)},
    {"title": "Final Presentation", "start_date": datetime(2024, 11, 26), "end_date": datetime(2024, 11, 27)},
]

# 폴더 경로 설정 및 폴더 생성 (존재하지 않으면 생성)
output_dir = "charts"
os.makedirs(output_dir, exist_ok=True)

# Generate Gantt Chart with a clean and simple design
fig, ax = plt.subplots(figsize=(12, 7))

# Define bar height and colors for a clean look
bar_height = 0.2
colors = ["#caf0f8", "#ade8f4", "#90e0ef", "#48cae4", "#00b4d8", "#0096c7", 
    "#0077b6", "#023e8a", "#03045e"]
today = datetime.now()

# Plot each issue as a simple bar
for i, issue in enumerate(issues):
    title = issue["title"]
    start_date = mdates.date2num(issue["start_date"])  # Convert start date to numerical format
    end_date = mdates.date2num(issue["end_date"])      # Convert end date to numerical format
    duration = end_date - start_date
    color = colors[i % len(colors)]
    
    # Draw the bar with ax.barh
    ax.barh(title, duration, left=start_date, color=color, height=bar_height, alpha=0.8)
    # Add text label for the duration on each bar
    ax.text(end_date, i, f'{int(duration)} days', va='center', ha='left', fontsize=9, color='black', alpha=0.8)

# Add today’s date line for reference
ax.axvline(mdates.date2num(today), color="red", linestyle="--", linewidth=1.2, label="Today")

# Set x-axis formatting for dates and enhance layout
ax.xaxis.set_major_locator(mdates.DayLocator(interval=2))
ax.xaxis.set_major_formatter(mdates.DateFormatter("%Y-%m-%d"))
plt.xticks(rotation=45, fontsize=9, color='gray')
plt.yticks(fontsize=10, color='gray')

# Title and labels with enhanced style
plt.title("Team Progress and Schedule", fontsize=16, weight='bold', color='#333333', pad=15)
plt.xlabel("Dates", fontsize=12, labelpad=10, color='gray')
plt.ylabel("Issues", fontsize=12, labelpad=10, color='gray')

# Add a legend for "Today" line
plt.legend(loc="upper right")

# Grid for readability
plt.grid(visible=True, axis='x', linestyle='--', color='gray', alpha=0.6)

# Adjust layout for a clean appearance
plt.tight_layout()

# Save the chart with today's date in the filename and in the specified folder
today_str = today.strftime("%Y%m%d")
output_path = os.path.join(output_dir, f"gantt_chart_{today_str}.png")
plt.savefig(output_path, format="png")
