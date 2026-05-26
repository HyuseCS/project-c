---
name: Project C
description: A premium, sophisticated student companion with a lucid, glassy aesthetic.
colors:
  primary: "#6200EE" # Lucid Indigo (Vibrant but deep)
  on-primary: "#FFFFFF"
  primary-container: "#F0E7FF"
  on-primary-container: "#21005D"
  surface: "#FCFCFF" # Lucid White (Cool-tinted neutral)
  on-surface: "#1A1C1E"
  surface-variant: "#E1E2EC" # Lucid Gray
  on-surface-variant: "#44474E"
  outline: "#74777F"
  error: "#B3261E"
typography:
  headline:
    fontFamily: "sans-serif"
    fontSize: "32sp"
    fontWeight: 800
    letterSpacing: "-0.02em"
  title:
    fontFamily: "sans-serif"
    fontSize: "20sp"
    fontWeight: 700
  body:
    fontFamily: "sans-serif"
    fontSize: "16sp"
    fontWeight: 400
    lineHeight: "24sp"
  label:
    fontFamily: "sans-serif"
    fontSize: "12sp"
    fontWeight: 600
    textTransform: "uppercase"
    letterSpacing: "0.05em"
rounded:
  sm: "12px"
  md: "20px"
  lg: "32px"
spacing:
  xs: "4px"
  sm: "12px"
  md: "24px"
  lg: "40px"
  xl: "64px"
components:
  lucid-glass:
    backgroundColor: "rgba(255, 255, 255, 0.7)"
    blur: "20px"
    rounded: "{rounded.lg}"
    border: "1px solid rgba(255, 255, 255, 0.3)"
  invisible-container:
    padding: "{spacing.md}"
    backgroundColor: "transparent"
---

# Design System: Project C (Lucid Glass)

## 1. Overview

**Creative North Star: "The Lucid Lens"**

Project C is a premium "Lucid Lens" through which students view and manage their lives. It moves away from the clutter of "basic" apps by utilizing **Invisible Hierarchy**—where information is separated by generous whitespace and bold typography rather than bounding boxes. For high-priority information, we use **Tactile Glass** components that feel layered and sophisticated.

**Key Characteristics:**
- **Invisible Containers**: Use whitespace and strong typographic scale (800 weight headlines) to define areas.
- **Lucid Color Strategy**: A neutral base with a single, high-contrast accent (Lucid Indigo) that signifies action and focus.
- **Tactile Depth**: Subtle transparency and layering create a sense of premium build quality.

## 2. Colors

A "Restrained" strategy that prioritizes focus and harmony.

### Primary: Lucid Indigo
- **Lucid Indigo** (#6200EE): A vibrant, deep purple that provides high contrast against the neutral base. Used for primary calls to action.

### Neutral: Lucid Neutrals
- **Lucid White** (#FCFCFF): A cool, crisp white that feels modern and premium.
- **Lucid Gray** (#E1E2EC): Used for subtle tonal layering and secondary backgrounds.

**The Contrast Rule.** The primary accent must maintain a minimum 4.5:1 contrast ratio (WCAG AA) against all surface colors to ensure accessibility and professional polish.

## 3. Typography

Typography is the primary UI driver. We use extreme weight and scale contrast.

### Hierarchy
- **Headline** (800, 32sp, -0.02em): Main titles. Large, bold, and authoritative.
- **Title** (700, 20sp): Section headers.
- **Body** (400, 16sp): Highly legible, with generous line-height (24sp).
- **Label** (600, 12sp, Uppercase): Technical metadata and small UI hints.

## 4. Elevation & Surface

**The Lucid Layering Rule.** Avoid solid shadows. Use transparency and tonal differences (Lucid White vs. Lucid Gray) to show depth. "Glassy" components should use a semi-transparent white base with a very subtle border.

## 5. Components

### Lucid Glass Surfaces
- **Shape**: Extra large rounded corners (32px).
- **Style**: 70% transparency with a subtle "inner glow" border (1px white at 30% opacity).
- **Use Case**: Primary Hero stats and floating action panels.

### Invisible List Items
- **Style**: No bounding box. Separated by MD spacing (24dp) and subtle dividers.

## 6. Do's and Don'ts

### Do:
- **Do** use massive whitespace (40dp - 64dp) between major sections.
- **Do** use typography as the primary means of grouping information.
- **Do** ensure every color choice feels intentional and harmonious.

### Don't:
- **Don't** use solid-color cards for every piece of data.
- **Don't** use "basic" consumer colors (safe blues, generic greens).
- **Don't** use tight, cluttered layouts.
- **Don't** use rounded corners smaller than 12px for prominent elements.
