---
name: Project C
description: A premium, sophisticated student companion with a "Mobile Cinema OLED" aesthetic.
colors:
  primary: "#A16207" # Lucid Gold (OLED Accent)
  on-primary: "#FFFFFF"
  secondary: "#1C1917" # Muted Slate (Surface Background)
  on-secondary: "#E8ECF0"
  background: "#0C0A09" # Deep OLED Black
  on-background: "#FFFFFF"
  surface: "#0C0A09"
  on-surface: "#FFFFFF"
  surface-variant: "#1E293B"
  on-surface-variant: "#F8FAFC"
  error: "#DC2626"
typography:
  headline:
    fontFamily: "sans-serif"
    fontSize: "32sp"
    fontWeight: 600
    letterSpacing: "-0.5sp"
  title:
    fontFamily: "sans-serif"
    fontSize: "24sp"
    fontWeight: 600
  body:
    fontFamily: "sans-serif"
    fontSize: "16sp"
    fontWeight: 400
  label:
    fontFamily: "sans-serif"
    fontSize: "12sp"
    fontWeight: 500
    letterSpacing: "1.2sp"
rounded:
  sm: "8px"
  md: "12px"
  lg: "32px"
spacing:
  xs: "4px"
  sm: "12px"
  md: "24px"
  lg: "40px"
  xl: "64px"
components:
  lucid-glass:
    backgroundColor: "rgba(255, 255, 255, 0.08)"
    blur: "15dp"
    rounded: "32dp"
    border: "1px solid rgba(255, 255, 255, 0.15)"
---\n\n# Design System: Project C (Mobile Cinema OLED)\n\n## 1. Overview\n\n**Creative North Star: "The Cinema Lens"**\n\nProject C utilizes a "Mobile Cinema" aesthetic—designed for high-end OLED displays and nighttime utility. The UI is built on a foundation of **Deep OLED Black**, prioritizing battery efficiency and eye comfort. We use **Lucid Gold** as a singular, precious accent color that cuts through the darkness, signifying action and importance.\n\n**Key Characteristics:**\n- **OLED First**: Pure black backgrounds (#0C0A09) to minimize light emission.\n- **Gold Standard**: A "Committed" color strategy where Lucid Gold carries the brand identity.\n- **Dark Tactile Glass**: Semi-transparent overlays use a white tint at 8% opacity to create depth on dark surfaces.\n\n## 2. Colors\n\n### Primary: Lucid Gold\n- **Lucid Gold** (#A16207): A rich, deep gold that feels premium and focused.\n\n### Neutral: OLED Neutrals\n- **Deep OLED** (#0C0A09): The core background color. Pure black.\n- **Muted Slate** (#1C1917): Used for card surfaces and secondary backgrounds to provide subtle contrast.\n\n## 3. Typography\n\n### Hierarchy\n- **Headline** (SemiBold, 32sp, -0.5sp): Bold and clear.\n- **Title** (SemiBold, 24sp): Section-level emphasis.\n- **Body** (Normal, 16sp): Highly legible.\n- **Label** (Medium, 12sp, 1.2sp): Metadata and system hints.\n\n## 4. Components\n\n### Lucid Glass (Dark Mode)\n- **Shape**: Large rounded corners (32dp).\n- **Style**: 8% white opacity with a 15% white border. This creates a "frosted glass" look that is visible on black backgrounds.\n\n### Interaction States\n- **Primary CTA**: Solid Lucid Gold with white text.\n- **Secondary Actions**: Translucent slate or glass surfaces.\n