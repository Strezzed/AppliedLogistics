/*
 *
 * LIMITED USE SOFTWARE LICENSE AGREEMENT
 * This Limited Use Software License Agreement (the "Agreement") is a legal agreement between you, the end-user, and the FlatstoneTech Team ("FlatstoneTech"). By downloading or purchasing the software material, which includes source code (the "Source Code"), artwork data, music and software tools (collectively, the "Software"), you are agreeing to be bound by the terms of this Agreement. If you do not agree to the terms of this Agreement, promptly destroy the Software you may have downloaded or copied.
 * FlatstoneTech SOFTWARE LICENSE
 * 1. Grant of License. FlatstoneTech grants to you the right to use the Software. You have no ownership or proprietary rights in or to the Software, or the Trademark. For purposes of this section, "use" means loading the Software into RAM, as well as installation on a hard disk or other storage device. The Software, together with any archive copy thereof, shall be destroyed when no longer used in accordance with this Agreement, or when the right to use the Software is terminated. You agree that the Software will not be shipped, transferred or exported into any country in violation of the U.S. Export Administration Act (or any other law governing such matters) and that you will not utilize, in any other manner, the Software in violation of any applicable law.
 * 2. Permitted Uses. For educational purposes only, you, the end-user, may use portions of the Source Code, such as particular routines, to develop your own software, but may not duplicate the Source Code, except as noted in paragraph 4. The limited right referenced in the preceding sentence is hereinafter referred to as "Educational Use." By so exercising the Educational Use right you shall not obtain any ownership, copyright, proprietary or other interest in or to the Source Code, or any portion of the Source Code. You may dispose of your own software in your sole discretion. With the exception of the Educational Use right, you may not otherwise use the Software, or an portion of the Software, which includes the Source Code, for commercial gain.
 * 3. Prohibited Uses: Under no circumstances shall you, the end-user, be permitted, allowed or authorized to commercially exploit the Software. Neither you nor anyone at your direction shall do any of the following acts with regard to the Software, or any portion thereof:
 * Rent;
 * Sell;
 * Lease;
 * Offer on a pay-per-play basis;
 * Distribute for money or any other consideration; or
 * In any other manner and through any medium whatsoever commercially exploit or use for any commercial purpose.
 * Notwithstanding the foregoing prohibitions, you may commercially exploit the software you develop by exercising the Educational Use right, referenced in paragraph 2. hereinabove.
 * 4. Copyright. The Software and all copyrights related thereto (including all characters and other images generated by the Software or depicted in the Software) are owned by FlatstoneTech and is protected by United States copyright laws and international treaty provisions. FlatstoneTech shall retain exclusive ownership and copyright in and to the Software and all portions of the Software and you shall have no ownership or other proprietary interest in such materials. You must treat the Software like any other copyrighted material. You may not otherwise reproduce, copy or disclose to others, in whole or in any part, the Software. You may not copy the written materials accompanying the Software. You agree to use your best efforts to see that any user of the Software licensed hereunder complies with this Agreement.
 * 5. NO WARRANTIES. FLATSTONETECH DISCLAIMS ALL WARRANTIES, BOTH EXPRESS IMPLIED, INCLUDING BUT NOT LIMITED TO, IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE WITH RESPECT TO THE SOFTWARE. THIS LIMITED WARRANTY GIVES YOU SPECIFIC LEGAL RIGHTS. YOU MAY HAVE OTHER RIGHTS WHICH VARY FROM JURISDICTION TO JURISDICTION. FlatstoneTech DOES NOT WARRANT THAT THE OPERATION OF THE SOFTWARE WILL BE UNINTERRUPTED, ERROR FREE OR MEET YOUR SPECIFIC REQUIREMENTS. THE WARRANTY SET FORTH ABOVE IS IN LIEU OF ALL OTHER EXPRESS WARRANTIES WHETHER ORAL OR WRITTEN. THE AGENTS, EMPLOYEES, DISTRIBUTORS, AND DEALERS OF FlatstoneTech ARE NOT AUTHORIZED TO MAKE MODIFICATIONS TO THIS WARRANTY, OR ADDITIONAL WARRANTIES ON BEHALF OF FlatstoneTech.
 * Exclusive Remedies. The Software is being offered to you free of any charge. You agree that you have no remedy against FlatstoneTech, its affiliates, contractors, suppliers, and agents for loss or damage caused by any defect or failure in the Software regardless of the form of action, whether in contract, tort, includinegligence, strict liability or otherwise, with regard to the Software. Copyright and other proprietary matters will be governed by United States laws and international treaties. IN ANY CASE, FlatstoneTech SHALL NOT BE LIABLE FOR LOSS OF DATA, LOSS OF PROFITS, LOST SAVINGS, SPECIAL, INCIDENTAL, CONSEQUENTIAL, INDIRECT OR OTHER SIMILAR DAMAGES ARISING FROM BREACH OF WARRANTY, BREACH OF CONTRACT, NEGLIGENCE, OR OTHER LEGAL THEORY EVEN IF FLATSTONETECH OR ITS AGENT HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES, OR FOR ANY CLAIM BY ANY OTHER PARTY. Some jurisdictions do not allow the exclusion or limitation of incidental or consequential damages, so the above limitation or exclusion may not apply to you.
 */

package tech.flatstone.appliedlogistics.common.grid;


import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.traverse.ClosestFirstIterator;
import tech.flatstone.appliedlogistics.common.util.GraphHelper;
import tech.flatstone.appliedlogistics.common.util.LogHelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

class GridServer implements Runnable {
    private ArrayList<UUIDPair> vertexCache; // Caches vertices that cannot currently be added
    private DirectedGraph<UUID, FilteredEdge> graph;
    private ConcurrentLinkedQueue<UUID> vertexIngestQueue;
    private ConcurrentLinkedQueue<UUIDPair> edgeIngestQueue;
    private ConcurrentLinkedQueue<UUIDPair> exitIngestQueue;
    private ConcurrentLinkedQueue<WhitelistData> whitelistDataQueue;
    private ConcurrentLinkedQueue<UUID> vertexEliminationQueue;
    private ConcurrentLinkedQueue<UUIDPair> edgeEliminationQueue;
    private ConcurrentLinkedQueue<UUIDPair> exitEliminationQueue;
    private CyclicBarrier barrier;
    private LinkedList<TransportContainer> activeCargo;
    private ConcurrentLinkedQueue<TransportContainer> incomingCargo;
    private ConcurrentHashMap<UUID, TransportContainer> outgoingCargo;
    private AtomicBoolean running;

    GridServer() {
        graph = new SimpleDirectedWeightedGraph<UUID, FilteredEdge>(
                new ClassBasedEdgeFactory<UUID, FilteredEdge>(FilteredEdge.class)
        );

        vertexIngestQueue = new ConcurrentLinkedQueue<UUID>();
        edgeIngestQueue = new ConcurrentLinkedQueue<UUIDPair>();
        exitIngestQueue = new ConcurrentLinkedQueue<UUIDPair>();
        whitelistDataQueue = new ConcurrentLinkedQueue<WhitelistData>();

        vertexEliminationQueue = new ConcurrentLinkedQueue<UUID>();
        edgeEliminationQueue = new ConcurrentLinkedQueue<UUIDPair>();
        exitEliminationQueue = new ConcurrentLinkedQueue<UUIDPair>();

        incomingCargo = new ConcurrentLinkedQueue<TransportContainer>();
        activeCargo = new LinkedList<TransportContainer>();
        outgoingCargo = new ConcurrentHashMap<UUID, TransportContainer>();

        if (vertexIngestQueue == null)
            throw new NullPointerException();

        if (edgeIngestQueue == null)
            throw new NullPointerException();

        barrier = new CyclicBarrier(2);

        vertexCache = new ArrayList<UUIDPair>();

        running = new AtomicBoolean();


    }

    void addCargo(TransportContainer objectContainer) {
        this.incomingCargo.offer(objectContainer);
    }

    TransportContainer getCargo(UUID exitNode) {
        return this.outgoingCargo.get(exitNode);
    }

    @Override
    public void run() {
        running.set(true);

        while (running.get()) {
            //sync with world server
            try {
                barrier.await();
            } catch (InterruptedException e) {
                LogHelper.fatal(e.getLocalizedMessage());
            } catch (BrokenBarrierException e) {
                LogHelper.fatal(e.getLocalizedMessage());
            }

            //step the transport simulation
            this.gridTick();
        }
    }

    /**
     * this method is called from the games main server thread in the server tick handler
     */
    public void sync() {
        try {
            barrier.await();
        } catch (InterruptedException e) {
            LogHelper.fatal(e.getLocalizedMessage());
        } catch (BrokenBarrierException e) {
            LogHelper.fatal(e.getLocalizedMessage());
        }
    }

    void gridTick() {

        //ingest vertex queue
        for (UUID id : vertexIngestQueue) {
            graph.addVertex(id);
            vertexIngestQueue.remove(id);
        }

        //handle a hopefully rare situation where a vertex and edge are added between ingest vertex and ingest edge
        if (!vertexCache.isEmpty()) {
            edgeIngestQueue.addAll(vertexCache);
            vertexCache.clear();
        }

        //ingest edge queue
        ingestEdgeQueue();

        for (UUIDPair pair : exitIngestQueue) {
            FilteredEdge edge = graph.getEdge(pair.getUUID1(), pair.getUUID2());
            if (edge == null)
                break;

            edge.setExit(true);
            exitIngestQueue.remove(pair);
        }

        //update grid objects
        for (TransportContainer container : activeCargo) {
            outgoingCargo.put(container.getDestination(), container);
            activeCargo.remove(container);
        }

        //apply whitelist/blacklists
        applyWhitelistBlacklist();

        //ingest objects
        precessObjects();

        //remove old edges
        trimEdges();

        //remove old vertex
        trimVertex();
    }

    private void ingestEdgeQueue() {
        for (UUIDPair pair : edgeIngestQueue) {
            if ((graph.containsVertex(pair.getUUID1())) && (graph.containsVertex(pair.getUUID2()))) {
                graph.addEdge(pair.getUUID1(), pair.getUUID2());
            } else {
                vertexCache.add(pair);
                LogHelper.debug("A vertex for edge:" + pair.getUUID1() + " -> " + pair.getUUID2() + " does not exist");
            }
            edgeIngestQueue.remove(pair);
        }
    }

    private void applyWhitelistBlacklist() {
        for (WhitelistData whitelistData : whitelistDataQueue) {
            FilteredEdge edge = graph.getEdge(whitelistData.getParent(), whitelistData.getEnd());
            if (edge == null)
                break;

            if (whitelistData.isWhitelist()) {
                edge.setWhitelist(whitelistData.getList());
            } else {
                edge.setBlacklist(whitelistData.getList());
            }
            whitelistDataQueue.remove(whitelistData);
        }
    }

    private void precessObjects() {
        for (TransportContainer container : incomingCargo) {
            ClosestFirstIterator<UUID, FilteredEdge> closestFirstIterator =
                    new ClosestFirstIterator<UUID, FilteredEdge>(
                            graph, container.getSource(), container.getSearchRange()
                    );

            UUID last = container.getSource();
            while (closestFirstIterator.hasNext()) {
                UUID uuid = closestFirstIterator.next();
                if (uuid == last)
                    continue;

                FilteredEdge edge = graph.getEdge(last, uuid);
                last = uuid;
                if ((edge.isExit()) && (edge.canRoute(container.getUnlocalizedName()))) {
                    container.setDestination(uuid);
                    container.setPath(GraphHelper.findPathBetween(closestFirstIterator, container.getSource(), uuid));
                }
            }

            activeCargo.add(container);
            incomingCargo.remove(container);
        }
    }

    private void trimEdges() {
        for (UUIDPair pair : edgeEliminationQueue) {
            if ((graph.containsVertex(pair.getUUID1())) && (graph.containsVertex(pair.getUUID2()))) {
                graph.removeAllEdges(pair.getUUID1(), pair.getUUID2());
            } else if (edgeIngestQueue.contains(pair)) {
                edgeIngestQueue.remove(pair);
            }
            edgeEliminationQueue.remove(pair);
        }
    }

    private void trimVertex() {
        for (UUID vertex : vertexEliminationQueue) {
            if (graph.containsVertex(vertex)) {
                graph.removeVertex(vertex);
            } else if (vertexIngestQueue.contains(vertex)) {
                vertexIngestQueue.remove(vertex);
            }
            vertexEliminationQueue.remove(vertex);
        }
    }

    boolean addVertex(UUID uuid) {
        return uuid != null && vertexIngestQueue.offer(uuid);
    }

    boolean removeVertex(UUID uuid) {
        return uuid != null && vertexEliminationQueue.offer(uuid);
    }

    boolean addEdge(UUID source, UUID destination) {
        return !((source == null) || (destination == null)) && edgeIngestQueue.offer(new UUIDPair(source, destination));
    }

    boolean removeEdge(UUID source, UUID destination) {
        return !((source == null) || (destination == null)) && exitEliminationQueue.offer(new UUIDPair(source, destination));
    }

    boolean markEdgeExit(UUID source, UUID destination) {
        return source != null && exitIngestQueue.offer(new UUIDPair(source, destination));
    }

    boolean applyFilter(boolean isWhitelist, UUID parent, UUID end, List<String> list) {
        return whitelistDataQueue.offer(new WhitelistData(isWhitelist, parent, end, list));
    }

    void stop() throws InterruptedException, BrokenBarrierException, TimeoutException {
        running.set(false);
        try {
            barrier.await(500, MILLISECONDS);
        } catch (InterruptedException e) {
            LogHelper.fatal(e.getLocalizedMessage());
            throw e;
        } catch (BrokenBarrierException e) {
            LogHelper.fatal((e.getLocalizedMessage()));
            throw e;
        } catch (TimeoutException e) {
            LogHelper.fatal(e.getLocalizedMessage());
            throw e;
        }
    }

    String serializeGraph() {
        if (running.get()) {
            return null;
        } else {
            return graph.toString();
        }
    }

    String serializeCargo() {
        if (running.get()) {
            return null;
        } else {
            return "this will be the cargo";
        }
    }
}
