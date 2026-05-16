# Project for the software engineering course

This is a Java 21 2D multiplayer battle royale game project created as part of a Software Engineering course project. The codebase is split into client, server, desktop, and shared core modules, with a focus on clear boundaries, testable components, and a maintainable multiplayer architecture.

## About

This project is built around a client-server game flow with shared protocol and game logic. The repository includes message definitions, networking code, rendering-facing client code, and server-side matchmaking and room management.

## Engineering Approach

The project is organized with Software Engineering principles in mind:

    TDD-friendly structure with unit tests supported by JUnit 5 and Mockito
    SOLID-oriented module and interface design
    Separation of concerns between frontend, core logic, networking, and server responsibilities
    Shared message protocols documented in docs/messages

These principles guide the codebase and make it easier to extend, test, and reason about.

## Project Structure

    client - frontend and view-model code for the game client
    core - shared game, networking, and utility logic
    server - server launcher, matchmaking, room, and session code
    desktop - desktop entry point for running the client
    messagetraitschecker - helper module for message-related tooling

## Tech Stack

    Java 21
    Gradle
    LibGDX
    JUnit 5
    Mockito

